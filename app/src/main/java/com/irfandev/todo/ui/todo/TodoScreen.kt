package com.irfandev.todo.ui.todo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.irfandev.todo.data.local.entity.TodoEntity
import com.irfandev.todo.viewmodel.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    modifier: Modifier = Modifier,
    todoViewModel: TodoViewModel
) {
    val todos by todoViewModel.todos.collectAsState()
    val allTodos by todoViewModel.allTodos.collectAsState() // For labels list
    var showAddDialog by remember { mutableStateOf(false) }
    var showFilterMenu by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf<TodoEntity?>(null) }

    val labels = remember(allTodos) { todoViewModel.getAllLabels(allTodos) }
    val selectedLabel by todoViewModel.selectedLabel.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("To-Do") },
                actions = {
                    IconButton(onClick = { showFilterMenu = !showFilterMenu }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter")
                    }

                    DropdownMenu(
                        expanded = showFilterMenu,
                        onDismissRequest = { showFilterMenu = false }
                    ) {
                        labels.forEach { label ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    todoViewModel.selectLabel(label)
                                    showFilterMenu = false
                                }
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (todos.isEmpty()) {
                Text(
                    text = if (selectedLabel == "All") "No tasks. Tap + to add one."
                    else "No tasks for '$selectedLabel' label.",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(todos, key = { it.id }) { todo ->
                        TodoRow(
                            todo = todo,
                            onClick = { selected = todo },
                            onToggle = { todoViewModel.markCompleted(todo) }
                        )
                    }
                }
            }

            // Add dialog
            if (showAddDialog) {
                AddTodoDialog(
                    onDismiss = { showAddDialog = false },
                    onSave = { title, desc, label ->
                        todoViewModel.addTodo(title, desc, label)
                        showAddDialog = false
                    }
                )
            }

            // Detail dialog
            selected?.let { todo ->
                AlertDialog(
                    onDismissRequest = { selected = null },
                    title = { Text(todo.title) },
                    text = {
                        Column {
                            Text(todo.description.ifEmpty { "No description" })
                            Spacer(modifier = Modifier.height(12.dp))
                            if (!todo.label.isNullOrBlank())
                                Text("Label: ${todo.label}", style = MaterialTheme.typography.labelSmall)
                            Text("Created: ${todo.createdAt}", style = MaterialTheme.typography.labelSmall)
                            todo.completedAt?.let {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Completed: $it", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    },
                    confirmButton = {
                        Row {
                            TextButton(onClick = { selected = null }) { Text("Close") }
                            Spacer(modifier = Modifier.width(8.dp))
                            TextButton(
                                onClick = {
                                    selected?.let { todoViewModel.deleteTodo(it) }
                                    selected = null
                                },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = MaterialTheme.colorScheme.error
                                )
                            ) { Text("Delete") }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun TodoRow(
    todo: TodoEntity,
    onClick: () -> Unit,
    onToggle: () -> Unit
) {
    val alpha = if (todo.completed) 0.5f else 1f

    Surface(
        tonalElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha)
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(todo.title, style = MaterialTheme.typography.titleMedium)
                if (todo.description.isNotEmpty()) {
                    Text(todo.description, style = MaterialTheme.typography.bodySmall, maxLines = 2)
                }
                Spacer(modifier = Modifier.height(6.dp))
                if (!todo.label.isNullOrBlank()) {
                    Text("Label: ${todo.label}", style = MaterialTheme.typography.labelSmall)
                }
                Text("Created: ${todo.createdAt}", style = MaterialTheme.typography.labelSmall)
                if (todo.completedAt != null) {
                    Text("Completed: ${todo.completedAt}", style = MaterialTheme.typography.labelSmall)
                }
            }

            Checkbox(
                checked = todo.completed,
                onCheckedChange = { if (!todo.completed) onToggle() },
                enabled = !todo.completed
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var label by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Task") },
        text = {
            Column {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = label, onValueChange = { label = it }, label = { Text("Label") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (title.isNotBlank()) {
                    onSave(title.trim(), desc.trim(), label.trim().ifEmpty { null })
                }
            }) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
