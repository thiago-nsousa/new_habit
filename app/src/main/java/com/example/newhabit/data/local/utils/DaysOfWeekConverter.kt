package com.example.newhabit.data.local.utils

import androidx.room.TypeConverter

class DaysOfWeekConverter {

    @TypeConverter
    fun fromDaysOfWeek(value: List<Int>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun toDaysOfWeek(value: String): List<Int> {
        return value.split(",").map { it.toInt() }
    }
}