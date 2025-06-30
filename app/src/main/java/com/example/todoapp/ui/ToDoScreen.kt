package com.example.todoapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
            Text("Dodaj zadanie")
        }

        LazyColumn {
            items(tasks) { task ->
                Text(text = task.title)
            }
        }
    }
}