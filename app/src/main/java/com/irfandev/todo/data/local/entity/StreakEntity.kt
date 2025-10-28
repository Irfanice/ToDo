package com.irfandev.todo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "streaks")
data class StreakEntity(
    @PrimaryKey
    val date: String,
    val status: String // "yes", "no", or "none"
)
