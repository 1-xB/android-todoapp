package com.example.todoapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.todoapp.data.Task
import com.example.todoapp.viewmodels.TaskViewModel
import kotlinx.coroutines.launch

@Composable
fun ToDoScreen(
    modifier: Modifier = Modifier,
    taskViewModel: TaskViewModel
) {
    var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    var isExpandedPending by remember { mutableStateOf(true) }
    var isExpandedCompleted by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { // Uruchomi się przy pierwszym renderowaniu
        tasks = taskViewModel.getAllTasks()
    }

    Column(modifier = modifier) {
        Button(
            onClick = {
                coroutineScope.launch { // Uruchomieie na żądanie
                    taskViewModel.addTask()
                    tasks = taskViewModel.getAllTasks() // Odśwież listę
                }
            }
        ) {
            Text("Add Task")
        }



        LazyColumn {

            item {
                Text(
                    text = "Your Tasks",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            item {
                Button(
                    onClick = { isExpandedPending = !isExpandedPending },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isExpandedPending) "Hide Pending Tasks ▲" else "Show Pending Tasks ▼"
                    )
                }
            }

            items(if (isExpandedPending) tasks.filter { it.isCompleted == false } else emptyList()) { task ->

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            text = task.description ?: "No description",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    taskViewModel.updateTaskCompletion(task.id, !task.isCompleted)
                                    tasks = taskViewModel.getAllTasks()
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                if (task.isCompleted) "Mark as Incomplete" else "Mark as Complete"
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Completed Tasks",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }



            item {
                Button(
                    onClick = { isExpandedCompleted = !isExpandedCompleted },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isExpandedCompleted) "Hide Completed Tasks ▲" else "Show Completed Tasks ▼"
                    )
                }
            }

            items(if (isExpandedCompleted) tasks.filter { it.isCompleted == true } else emptyList()) { task ->

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            text = task.description ?: "No description",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    taskViewModel.updateTaskCompletion(task.id, !task.isCompleted)
                                    tasks = taskViewModel.getAllTasks()
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                if (task.isCompleted) "Mark as Incomplete" else "Mark as Complete"
                            )
                        }
                    }
                }
            }
        }




    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowAddTaskDialog(
    onDismiss: () -> Unit,
    onAddTask: (Task) -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        content = {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.background,
                shadowElevation = 6.dp,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "Add New Task",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally),
                        color = MaterialTheme.colorScheme.onBackground
                    )


                    Spacer(
                        modifier = Modifier
                            .padding(8.dp)
                    )
                    var taskTitle by remember { mutableStateOf("") }
                    var taskDescription by remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = taskTitle,
                        onValueChange = { taskTitle = it },
                        label = { Text("Task Title") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = taskDescription,
                        onValueChange = { taskDescription = it },
                        label = { Text("Task Description") },
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Button(
                        onClick = {
                            if (taskTitle.isNotBlank()) {
                                onAddTask(Task(title = taskTitle, description = taskDescription))
                                onDismiss()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Add Task")
                    }

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    )
}