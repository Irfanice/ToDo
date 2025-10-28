package com.irfandev.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irfandev.todo.data.local.entity.TodoEntity
import com.irfandev.todo.data.repository.TodoRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    private val _selectedLabel = MutableStateFlow("All")
    val selectedLabel: StateFlow<String> = _selectedLabel.asStateFlow()

    val allTodos = repository.getAllTodos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val todos = combine(allTodos, _selectedLabel) { list, label ->
        if (label == "All") list else list.filter { it.label == label }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun addTodo(title: String, description: String, label: String?) {
        val now = dateFormat.format(Date())
        val todo = TodoEntity(
            title = title,
            description = description,
            completed = false,
            createdAt = now,
            completedAt = null,
            label = label
        )
        viewModelScope.launch {
            repository.insert(todo)
        }
    }

    fun markCompleted(todo: TodoEntity) {
        if (todo.completed) return
        val now = dateFormat.format(Date())
        viewModelScope.launch {
            repository.setCompleted(todo.id, true, now)
        }
    }

    fun deleteTodo(todo: TodoEntity) {
        viewModelScope.launch {
            repository.delete(todo)
        }
    }

    fun selectLabel(label: String) {
        _selectedLabel.value = label
    }

    fun getAllLabels(todos: List<TodoEntity>): List<String> {
        val labels = todos.mapNotNull { it.label }.distinct().sorted()
        return listOf("All") + labels
    }
}
