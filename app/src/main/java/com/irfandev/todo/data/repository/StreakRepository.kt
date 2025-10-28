package com.irfandev.todo.data.repository

import com.irfandev.todo.data.local.dao.StreakDao
import com.irfandev.todo.data.local.entity.StreakEntity
import kotlinx.coroutines.flow.Flow

class StreakRepository(private val dao: StreakDao) {
    fun getAllStreaks(): Flow<List<StreakEntity>> = dao.getAllStreaks()
    fun getStreakByDate(date: String): Flow<StreakEntity?> = dao.getStreakByDate(date)
    suspend fun insert(streak: StreakEntity) = dao.insert(streak)
}
