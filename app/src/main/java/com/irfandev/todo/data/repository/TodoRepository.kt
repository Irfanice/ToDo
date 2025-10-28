package com.irfandev.todo.data.repository

import com.irfandev.todo.data.local.dao.TodoDao
import com.irfandev.todo.data.local.entity.TodoEntity
import kotlinx.coroutines.flow.Flow

class TodoRepository(private val dao: TodoDao) {

    fun getAllTodos(): Flow<List<TodoEntity>> = dao.getAllTodos()

    suspend fun insert(todo: TodoEntity) = dao.insert(todo)

    suspend fun update(todo: TodoEntity) = dao.update(todo)

    suspend fun setCompleted(id: Int, completed: Boolean, completedAt: String?) =
        dao.setCompleted(id, completed, completedAt)

    suspend fun delete(todo: TodoEntity) = dao.delete(todo) // ✅ new
}

