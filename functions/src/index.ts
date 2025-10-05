import {
  onDocumentWritten,
  FirestoreEvent,
  Change,
  DocumentSnapshot,
} from "firebase-functions/v2/firestore";
import {onSchedule, ScheduledEvent} from "firebase-functions/v2/scheduler";
import * as admin from "firebase-admin";
import * as logger from "firebase-functions/logger";

const projectId = "app-new-habit";
process.env.GCLOUD_PROJECT = projectId;
admin.initializeApp({projectId: projectId});

const REGION = "southamerica-east1";

/**
 * Envia notificações diretamente via HTTP REST API.
 * @param {string[]} tokens - Uma lista de tokens FCM para os quais enviar.
 * @param {admin.messaging.MessagingPayload} payload - Conteúdo da notificação.
 */
async function sendNotificationViaHttp(
  tokens: string[],
  payload: admin.messaging.MessagingPayload
) {
  try {
    const accessToken = (
      await admin.credential.applicationDefault().getAccessToken()
    ).access_token;

    const fcmEndpoint =
      `https://fcm.googleapis.com/v1/projects/${projectId}/messages:send`;

    for (const token of tokens) {
      const message = {
        message: {
          token: token,
          notification: payload.notification,
        },
      };

      const response = await fetch(fcmEndpoint, {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${accessToken}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(message),
      });

      if (!response.ok) {
        const responseData = await response.json();
        logger.error(
          `Erro HTTP ao enviar para ${token}:`,
          response.status,
          responseData
        );
      } else {
        logger.info(`Notificação enviada via HTTP para ${token} com sucesso.`);
      }
    }
  } catch (error) {
    logger.error("Falha crítica ao enviar notificação via HTTP:", error);
  }
}

export const sendCompletionCongrats = onDocumentWritten(
  {
    document: "/users/{userId}/habits/{id}/progress/{id}",
    region: REGION,
  },
  async (event: FirestoreEvent<Change<DocumentSnapshot> | undefined>) => {
    logger.log("Função sendCompletionCongrats ACIONADA!");

    if (!event.data?.after) {
      logger.info("Evento sem dados (provavelmente um delete). A sair.");
      return;
    }

    const progressData = event.data.after.data();
    const userId = event.params.userId;

    if (!progressData) {
      logger.error("Dados do progresso estão vazios.");
      return;
    }

    const dateKeyFromDoc = progressData.dateKey;
    if (!dateKeyFromDoc) {
      logger.error("O campo 'dateKey' não existe no progresso.");
      return;
    }

    const userHabitsRef = admin.firestore()
      .collection("users").doc(userId).collection("habits");
    const dayOfWeek = new Date(dateKeyFromDoc).getUTCDay() + 1;

    const habitsForDaySnapshot = await userHabitsRef
      .where("daysOfWeek", "array-contains", dayOfWeek).get();
    const totalHabitsForToday = habitsForDaySnapshot.size;

    if (totalHabitsForToday === 0) {
      logger.info(`Utilizador ${userId} não tem hábitos para este dia.`);
      return;
    }

    let completedHabitsForToday = 0;
    for (const habitDoc of habitsForDaySnapshot.docs) {
      const progressSnapshot = await habitDoc.ref.collection("progress")
        .where("dateKey", "==", dateKeyFromDoc).limit(1).get();
      if (!progressSnapshot.empty) {
        completedHabitsForToday++;
      }
    }

    if (
      totalHabitsForToday > 0 &&
      totalHabitsForToday === completedHabitsForToday
    ) {
      logger.info(
        `Utilizador ${userId} completou todos os hábitos!`
      );
      const tokensSnapshot = await admin.firestore()
        .collection("users").doc(userId).collection("tokens").get();

      if (tokensSnapshot.empty) {
        logger.warn(
          `Nenhum token FCM encontrado para o utilizador ${userId}.`
        );
        return;
      }
      const tokens = tokensSnapshot.docs.map((doc) => doc.id);

      const payload: admin.messaging.MessagingPayload = {
        notification: {
          title: "Parabéns! 🎉",
          body: "Você completou todos os seus hábitos de hoje. " +
            "Continue assim!",
        },
      };

      await sendNotificationViaHttp(tokens, payload);
    } else {
      logger.info(
        `Progresso para ${userId}: ` +
        `${completedHabitsForToday}/${totalHabitsForToday}`
      );
    }
  });

export const sendDailyReminder = onSchedule(
  {
    schedule: "every day 21:00",
    timeZone: "America/Sao_Paulo",
    region: REGION,
  },
  async (event: ScheduledEvent) => {
    logger.info("A executar o lembrete diário das 21:00.", event);

    const today = new Date();
    // Ajustar para o fuso horário de São Paulo, se necessário
    const saoPauloDate = new Date(today.toLocaleString("en-US", {
      timeZone: "America/Sao_Paulo",
    }));
    const todayKey = saoPauloDate.toISOString().slice(0, 10);
    const dayOfWeek = saoPauloDate.getUTCDay() + 1;

    const usersSnapshot = await admin.firestore().collection("users").get();

    for (const userDoc of usersSnapshot.docs) {
      const userId = userDoc.id;

      const habitsForDaySnapshot = await userDoc.ref.collection("habits")
        .where("daysOfWeek", "array-contains", dayOfWeek).get();
      const totalHabitsForToday = habitsForDaySnapshot.size;

      if (totalHabitsForToday === 0) {
        continue;
      }

      let completedHabitsForToday = 0;
      for (const habitDoc of habitsForDaySnapshot.docs) {
        const progressSnapshot = await habitDoc.ref.collection("progress")
          .where("dateKey", "==", todayKey).limit(1).get();
        if (!progressSnapshot.empty) {
          completedHabitsForToday++;
        }
      }

      if (completedHabitsForToday < totalHabitsForToday) {
        logger.info(
          `Utilizador ${userId} tem hábitos pendentes. A enviar lembrete.`
        );

        const tokensSnapshot = await userDoc.ref.collection("tokens").get();
        if (tokensSnapshot.empty) {
          logger.warn(
            `Nenhum token FCM encontrado para o utilizador ${userId}.`
          );
          continue;
        }
        const tokens = tokensSnapshot.docs.map((doc) => doc.id);

        const payload: admin.messaging.MessagingPayload = {
          notification: {
            title: "Ainda dá tempo! 💪",
            body: "Não se esqueça de completar os seus hábitos de hoje.",
          },
        };

        await sendNotificationViaHttp(tokens, payload);
      }
    }
  });
