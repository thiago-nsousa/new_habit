package com.example.newhabit.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.newhabit.data.local.database.entity.HabitEntity

@Dao
interface HabitDao {

    @Query("SELECT * FROM habit")
    suspend fun fetchAll(): List<HabitEntity>

    @Query("SELECT * FROM habit WHERE daysOfWeek LIKE '%'||:dayOfWeek||'%'")
    suspend fun fetchByDayOfWeek(dayOfWeek: Int): List<HabitEntity>

    @Query(
        """
    SELECT * FROM habit 
    WHERE uuid = :habitId
    """
    )
    suspend fun fetchHabitById(habitId: String): HabitEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: HabitEntity)

    @Delete
    suspend fun delete(habit: HabitEntity)

    @Update
    suspend fun update(habit: HabitEntity)
}