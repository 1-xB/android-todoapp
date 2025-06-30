package com.example.todoapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.todoapp.data.Task
import com.example.todoapp.model.getTasks
import com.example.todoapp.viewmodels.TaskViewModel
import kotlinx.coroutines.launch

@Composable
fun ToDoScreen(
    modifier: Modifier = Modifier,
    taskViewModel: TaskViewModel
) {
    var tasks by remember { mutableStateOf<getTasks>(
        getTasks()
    ) }
    val coroutineScope = rememberCoroutineScope()

    var isExpandedPending by remember { mutableStateOf(true) }
    var isExpandedCompleted by remember { mutableStateOf(false) }

    var isAddTaskDialogOpen by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { // Uruchomi się przy pierwszym renderowaniu
        tasks = refreshTasks(taskViewModel = taskViewModel)
    }

    Column(modifier = modifier) {
        Button(
            onClick = {
                isAddTaskDialogOpen = true
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

            if (isExpandedPending) {
                if (tasks.pendingTasks.isEmpty()) {
                    item {
                        Text(
                            text = "No pending tasks",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                else {
                    items( tasks.pendingTasks) { task ->

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = task.isCompleted,
                                    onCheckedChange = {
                                        coroutineScope.launch {
                                            taskViewModel.updateTaskCompletion(task.id, !task.isCompleted)
                                            tasks = refreshTasks(taskViewModel = taskViewModel)
                                        }
                                    },
                                    modifier = Modifier.padding(end = 8.dp),
                                )

                                Column {
                                    Text(
                                        text = task.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = task.description ?: "No description",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
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

            if (isExpandedCompleted) {
                if (tasks.completedTasks.isEmpty()) {
                    item {
                        Text(
                            text = "No completed tasks",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                else {
                    items(tasks.completedTasks) { task ->

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = task.isCompleted,
                                    onCheckedChange = {
                                        coroutineScope.launch {
                                            taskViewModel.updateTaskCompletion(task.id, !task.isCompleted)
                                            tasks = refreshTasks(taskViewModel = taskViewModel)
                                        }
                                    },
                                    modifier = Modifier.padding(end = 8.dp),
                                )

                                Column {
                                    Text(
                                        text = task.title,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            textDecoration = TextDecoration.LineThrough
                                        ),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                    )
                                    Text(
                                        text = task.description ?: "No description",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            textDecoration = TextDecoration.LineThrough
                                        ),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        }
                    }
                }
                }
            }

        }


    if (isAddTaskDialogOpen) {
        ShowAddTaskDialog(
            onDismiss = { isAddTaskDialogOpen = false },
            onAddTask = { newTask ->
                coroutineScope.launch {
                    taskViewModel.addTask(newTask)
                    tasks = refreshTasks(taskViewModel = taskViewModel)
                    isAddTaskDialogOpen = false
                }
            }
        )
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
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        content = {
            Surface(
                shape = MaterialTheme.shapes.large,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Add New Task",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 16.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    var taskTitle by remember { mutableStateOf("") }
                    var taskDescription by remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = taskTitle,
                        onValueChange = { taskTitle = it },
                        label = { Text("Title") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )

                    OutlinedTextField(
                        value = taskDescription,
                        onValueChange = { taskDescription = it },
                        label = { Text("Description") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 80.dp)
                            .padding(vertical = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = onDismiss,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("Cancel")
                        }

                        Button(
                            onClick = {
                                if (taskTitle.isNotBlank()) {
                                    onAddTask(Task(title = taskTitle, description = taskDescription))
                                    onDismiss()
                                }
                            }
                        ) {
                            Text("Add")
                        }
                    }
                }
            }
        }
    )

}

suspend fun refreshTasks(
    taskViewModel: TaskViewModel,
) : getTasks {
    val tasks = taskViewModel.getAllTasks()

    return getTasks(
        completedTasks = tasks.filter { it.isCompleted == true },
        pendingTasks = tasks.filter { it.isCompleted == false }
    )
}