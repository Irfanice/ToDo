package com.irfandev.todo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val completed: Boolean = false,
    val createdAt: String,
    val completedAt: String? = null,
    val label: String? = null   // 🆕 new
)

