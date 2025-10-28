package com.irfandev.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.irfandev.todo.data.local.AppDatabase
import com.irfandev.todo.data.repository.*
import com.irfandev.todo.ui.screens.MainScreen
import com.irfandev.todo.ui.splash.SplashScreen
import com.irfandev.todo.ui.theme.ToDoTheme
import com.irfandev.todo.viewmodel.*

class MainActivity : ComponentActivity() {

    private lateinit var todoViewModel: TodoViewModel
    private lateinit var streakViewModel: StreakViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Room Database
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "todo_database"
        ).fallbackToDestructiveMigration().build()

        // Initialize Repositories
        val todoRepository = TodoRepository(db.todoDao())
        val streakRepository = StreakRepository(db.streakDao())

        // Initialize ViewModels
        val viewModelFactory = ViewModelFactory(
            todoRepository = todoRepository,
            streakRepository = streakRepository
        )
        todoViewModel = ViewModelProvider(this, viewModelFactory)[TodoViewModel::class.java]
        streakViewModel = ViewModelProvider(this, viewModelFactory)[StreakViewModel::class.java]

        // ✅ Compose content starts here
        setContent {
            ToDoTheme {
                var showSplash by remember { mutableStateOf(true) }

                Surface(color = MaterialTheme.colorScheme.background) {
                    if (showSplash) {
                        SplashScreen(onSplashFinished = { showSplash = false })
                    } else {
                        MainScreen(
                            todoViewModel = todoViewModel,
                            streakViewModel = streakViewModel
                        )
                    }
                }
            }
        }
    }
}
