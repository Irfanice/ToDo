package com.irfandev.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.irfandev.todo.data.repository.*

class ViewModelFactory(
    private val todoRepository: TodoRepository? = null,
    private val streakRepository: StreakRepository? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TodoViewModel::class.java) ->
                TodoViewModel(todoRepository!!) as T
            modelClass.isAssignableFrom(StreakViewModel::class.java) ->
                StreakViewModel(streakRepository!!) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
