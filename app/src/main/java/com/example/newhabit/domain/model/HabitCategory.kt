package com.example.newhabit.domain.model

import com.example.newhabit.R

enum class HabitCategory(val label: String) {
    HEALTH("Saúde"),
    WORK("Trabalho"),
    LEARNING("Aprendizado"),
    PERSONAL("Pessoal"),
    OTHER("Outros");

    fun getColorRes() = when (this) {
        HEALTH -> R.color.category_health
        WORK -> R.color.category_work
        LEARNING -> R.color.category_learning
        PERSONAL -> R.color.category_personal
        OTHER -> R.color.category_other
    }

    fun getOnColorRes() = when (this) {
        HEALTH -> R.color.on_category_health
        WORK -> R.color.on_category_work
        LEARNING -> R.color.on_category_learning
        PERSONAL -> R.color.on_category_personal
        OTHER -> R.color.on_category_other
    }

    fun getIconRes() = when (this) {
        HEALTH -> R.drawable.ic_health
        WORK -> R.drawable.ic_work
        LEARNING -> R.drawable.ic_learning
        PERSONAL -> R.drawable.ic_person
        OTHER -> R.drawable.ic_other
    }

    companion object {
        fun fromLabel(label: String): HabitCategory =
            values().firstOrNull { it.label == label } ?: OTHER
    }
}

