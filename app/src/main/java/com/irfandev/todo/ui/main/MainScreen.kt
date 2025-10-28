package com.irfandev.todo.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.irfandev.todo.ui.calendar.CalendarScreen
import com.irfandev.todo.ui.todo.TodoScreen
import com.irfandev.todo.viewmodel.*

@Composable
fun MainScreen(
    todoViewModel: TodoViewModel,
    streakViewModel: StreakViewModel
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val tabs = listOf("Streak", "To-Do")
    val icons = listOf(Icons.Default.CalendarToday, Icons.AutoMirrored.Filled.List)

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, label ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = label) },
                        label = { Text(label) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            0 -> CalendarScreen(
                modifier = Modifier.padding(innerPadding),
                streakViewModel = streakViewModel
            )
            1 -> TodoScreen(
                modifier = Modifier.padding(innerPadding),
                todoViewModel = todoViewModel
            )
        }
    }
}
