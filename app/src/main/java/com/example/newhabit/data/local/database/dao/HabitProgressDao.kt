package com.example.newhabit.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.newhabit.data.local.database.entity.HabitProgressEntity

@Dao
interface HabitProgressDao {

    @Query(
        """
    SELECT * FROM progress 
    WHERE habitId LIKE :habitId 
    AND DATE(completedAt/1000, 'unixepoch') = DATE(:completedAt/1000, 'unixepoch')
    """
    )
    suspend fun fetchProgressByHabit(habitId: String, completedAt: Long): List<HabitProgressEntity>

    @Insert
    suspend fun insert(progress: HabitProgressEntity)

    @Query(
        """
    DELETE FROM progress WHERE uuid = :progressId
    """
    )
    suspend fun delete(progressId: String)
}