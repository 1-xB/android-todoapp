package com.example.todoapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.todoapp.viewmodels.TaskViewModel

@Composable
fun ToDoApp(
    modifier: Modifier,
    taskViewModel: TaskViewModel
) {
    ToDoScreen(modifier = modifier,
        taskViewModel = taskViewModel
    )
}