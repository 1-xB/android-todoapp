package com.example.todoapp.model

import com.example.todoapp.data.Task

data class getTasks(
    val completedTasks: List<Task> = emptyList(),
    val pendingTasks: List<Task> = emptyList(),
)
