package com.example.newhabit.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newhabit.data.local.dao.HabitDao
import com.example.newhabit.data.local.dao.HabitProgressDao
import com.example.newhabit.data.local.entity.HabitEntity
import com.example.newhabit.data.local.entity.HabitProgressEntity
import com.example.newhabit.data.local.utils.DaysOfWeekConverter


@Database(entities = [HabitEntity::class, HabitProgressEntity::class], version = 1, exportSchema = false)
@TypeConverters(DaysOfWeekConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun progressDao(): HabitProgressDao

    abstract fun habitDao(): HabitDao

    companion object {

        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, DATABASE_NAME
                    )
                        .build()
                }
            }
            return instance!!
        }

        private const val DATABASE_NAME = "app-database.db"
    }
}