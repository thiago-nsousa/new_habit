package com.example.newhabit.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.newhabit.data.local.entity.HabitEntity

@Dao
interface HabitDao {

    @Query("SELECT * FROM habit WHERE daysOfWeek LIKE '%'||:dayOfWeek||'%'")
    suspend fun fetchByDayOfWeek(dayOfWeek: Int): List<HabitEntity>

    @Insert
    suspend fun insert(habit: HabitEntity)
}