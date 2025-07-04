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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
    taskViewModel: TaskViewModel,
    onNavigateToEditTask: (Int) -> Unit = {},
) {
    var tasks by remember { mutableStateOf(
        getTasks()
    ) }
    val coroutineScope = rememberCoroutineScope()

    var isExpandedCompleted by remember { mutableStateOf(false) }

    var isAddTaskDialogOpen by remember { mutableStateOf(false) }

    var taskToDelete by remember { mutableStateOf<Task?>(null) }

    LaunchedEffect(Unit) { // Uruchomi się przy pierwszym renderowaniu
        tasks = refreshTasks(taskViewModel = taskViewModel)
    }

    Column(modifier = modifier) {
        IconButton(
            onClick = {
                isAddTaskDialogOpen = true
            }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Task",
                tint = MaterialTheme.colorScheme.primary
            )
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
                        onClick = {
                            onNavigateToEditTask(task.id)
                        },
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
                                    text = if (task.title.length > 25) task.title.substring(0, 25) + "..." else task.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (task.description.isNullOrBlank()) "No description" else if (task.description.length > 25) task.description.substring(0, 25) + "..." else task.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                            }
                        }
                    }
                }
            }



            item {
                Button(
                    onClick = { isExpandedCompleted = !isExpandedCompleted },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = if (isExpandedCompleted) "▲ Completed Tasks ${if (tasks.completedTasks.isNotEmpty()) tasks.completedTasks.size else ""}" else "▼ Completed Tasks ${if (tasks.completedTasks.isNotEmpty()) tasks.completedTasks.size else ""}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
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
                            onClick = {
                                onNavigateToEditTask(task.id)
                            },
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
                                        text = if (task.title.length > 25) task.title.substring(0, 25) + "..." else task.title,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            textDecoration = TextDecoration.LineThrough
                                        ),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                    )
                                    Text(
                                        text = if (task.description.isNullOrBlank()) "No description" else if (task.description.length > 25) task.description.substring(0, 25) + "..." else task.description,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            textDecoration = TextDecoration.LineThrough
                                        ),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                    )
                                }

                                Spacer(modifier = Modifier.weight(1f))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End,
                                    modifier = Modifier.fillMaxWidth()
                                ) {

                                    IconButton(
                                        onClick = {
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "edit task",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            taskToDelete = task
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "delete task",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }

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

    if (taskToDelete != null) {
        ShowDeleteConfirmationDialog(
            onDismiss = {
                taskToDelete = null
            },
            onConfirmDelete = {
                coroutineScope.launch {
                    taskViewModel.deleteTask(taskToDelete!!.id)
                    tasks = refreshTasks(taskViewModel = taskViewModel)
                    taskToDelete = null
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

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ShowDeleteConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit
)
{
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
            )
            {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Are you sure you want to delete this task?",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 16.dp),
                        color = MaterialTheme.colorScheme.onSurface
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
                                onConfirmDelete()
                            }
                        ) {
                            Text("Delete")
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
        completedTasks = tasks.filter { it.isCompleted },
        pendingTasks = tasks.filter { !it.isCompleted  }
    )
}