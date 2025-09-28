package com.example.newhabit.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.newhabit.data.local.utils.DaysOfWeekConverter

@Entity(tableName = "habit")
data class HabitEntity(
    @PrimaryKey val uuid: String,
    @ColumnInfo(name = "title") val title: String,
    @TypeConverters(DaysOfWeekConverter::class)
    @ColumnInfo(name = "daysOfWeek") val daysOfWeek: List<Int>,
    @ColumnInfo(name = "category") val category: String,
)
