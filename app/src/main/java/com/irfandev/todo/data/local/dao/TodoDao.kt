package com.irfandev.todo.data.local.dao

import androidx.room.*
import com.irfandev.todo.data.local.entity.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM todos ORDER BY id DESC")
    fun getAllTodos(): Flow<List<TodoEntity>>

    @Query("UPDATE todos SET completed = :completed, completedAt = :completedAt WHERE id = :id")
    suspend fun setCompleted(id: Int, completed: Boolean, completedAt: String?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: TodoEntity)

    @Update
    suspend fun update(todo: TodoEntity)

    @Delete
    suspend fun delete(todo: TodoEntity)
}
