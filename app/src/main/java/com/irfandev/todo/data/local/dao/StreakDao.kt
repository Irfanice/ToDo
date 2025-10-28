package com.irfandev.todo.data.local.dao

import androidx.room.*
import com.irfandev.todo.data.local.entity.StreakEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StreakDao {

    @Query("SELECT * FROM streaks WHERE date = :date LIMIT 1")
    fun getStreakByDate(date: String): Flow<StreakEntity?>

    @Query("SELECT * FROM streaks")
    fun getAllStreaks(): Flow<List<StreakEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(streak: StreakEntity)
}
