# Habits App - Android
Esta é uma aplicação Android completa para monitorização de hábitos, desenvolvida com as tecnologias e arquiteturas mais modernas recomendadas pelo Google. O projeto serve como um exemplo robusto de como construir uma aplicação escalável, reativa e com funcionalidades de backend usando o ecossistema Firebase.

<img width="300" alt="Screenshot_20251005_190351" src="https://github.com/user-attachments/assets/051a6285-ca4e-4380-9310-2d2505f815b3" />

<img width="300" alt="Screenshot_20250928_191555" src="https://github.com/user-attachments/assets/3040c5bd-7d1b-4b31-87df-2c992b3b5fbc" />

<img width="300" alt="Screenshot_20250928_191537" src="https://github.com/user-attachments/assets/5e0feff7-29ad-496b-b345-2c29ab170f1c" />

<img width="300" alt="Screenshot_20251005_150217" src="https://github.com/user-attachments/assets/90a86feb-1ccd-4330-a0fa-a465b09c765f" />


## ✨ Funcionalidades
- Autenticação de Utilizador: Login e registo com E-mail/Senha e Google Sign-In.

- Gestão de Hábitos: Crie, edite e elimine hábitos personalizados.

- Monitorização de Progresso: Marque hábitos como concluídos para cada dia.

- Lembretes Locais: Agende notificações diárias para cada hábito, garantindo que o utilizador não se esqueça.

- Notificações Inteligentes (Backend):

   - Envia uma notificação de parabéns quando o utilizador completa todos os hábitos do dia.

   - Envia um lembrete diário às 21:00 para os utilizadores com hábitos pendentes.

- Tema Dinâmico: Suporte completo para temas Light e Dark, com a opção de seguir o tema do sistema.

- Interface Moderna: UI construída com Material Design 3.

## 🛠️ Arquitetura e Tecnologias
O projeto segue uma arquitetura limpa, separando as responsabilidades em camadas distintas (UI, Presentation, Domain, Data) para garantir um código desacoplado, testável e fácil de manter.

- Programação Assíncrona: Kotlin Coroutines e Flow para gerir operações em segundo plano e fluxos de dados reativos.

- Injeção de Dependência: Hilt para gerir o ciclo de vida e a injeção de dependências em toda a aplicação.

- Navegação: Navigation Component para gerir a navegação entre os fragments através de um Navigation Graph.

- Persistência Local: DataStore para salvar as preferências do utilizador (como o tema).

- Tarefas em Segundo Plano: WorkManager para o agendamento de notificações locais.

Stack Tecnológica - Backend (Firebase)
Plataforma: Firebase

- Autenticação: Firebase Authentication para uma gestão de utilizadores segura.

- Base de Dados: Cloud Firestore como base de dados NoSQL em tempo real para armazenar os dados dos utilizadores, hábitos, progressos e tokens de notificação.

- Funções Serverless: Cloud Functions for Firebase (v2), escritas em TypeScript, para automatizar a lógica de backend:

  - sendCompletionCongrats: Um gatilho do Firestore (onDocumentWritten) que é executado sempre que um progresso é registado.

  - sendDailyReminder: Uma função agendada (onSchedule) que é executada diariamente.

- Notificações Push: Firebase Cloud Messaging (FCM) para o envio de notificações remotas a partir das Cloud Functions.

## 🚀 Configuração do Projeto
- Clone o repositório.

- Abra o projeto no Android Studio.

- Crie um projeto na Firebase Console.

- Adicione uma aplicação Android ao seu projeto Firebase, usando o package name do projeto.

- Descarregue o ficheiro google-services.json e coloque-o na pasta app/ do seu projeto Android.

- Na Firebase Console, ative os seguintes serviços:

  - Authentication: Ative os provedores "E-mail/Senha" e "Google".

  - Firestore Database: Crie uma base de dados (a região southamerica-east1 é recomendada para o Brasil).

  - Obtenha a sua chave SHA-1 de depuração e adicione-a às configurações da sua app Android no Firebase para o login com Google funcionar.

  - Compile e execute a aplicação.

- Cloud Functions
  - Instale a Firebase CLI globalmente:

  ```
  
  npm install -g firebase-tools
  
  ```

  -  Navegue até à pasta functions no seu terminal.

  - Instale as dependências:
  ```

    npm install

  ```

  - Abra o ficheiro functions/src/index.ts e verifique se o projectId e a REGION estão corretos para o seu projeto.

  - Faça o deploy das funções:
  ```
  
    firebase deploy --only functions
  
  ```
