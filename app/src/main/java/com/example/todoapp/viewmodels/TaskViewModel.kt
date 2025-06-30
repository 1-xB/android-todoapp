package com.example.todoapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.todoapp.data.Task
import com.example.todoapp.repository.TaskRepository

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    suspend fun getAllTasks() = repository.getAllTasks()

    suspend fun addTask(task: Task = Task(
        title = "New Task",
        description = null,
        isCompleted = false,
    )) {
        repository.addTask(task)
    }
}