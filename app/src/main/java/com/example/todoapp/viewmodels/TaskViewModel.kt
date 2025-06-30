package com.example.todoapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.todoapp.data.Task
import com.example.todoapp.repository.TaskRepository

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    suspend fun getAllTasks() = repository.getAllTasks()

    suspend fun addTask(task: Task) {
        repository.addTask(task)
    }

    suspend fun updateTaskCompletion(taskId: Int, isCompleted: Boolean) {
        repository.updateTaskCompletion(taskId, isCompleted)
    }

    suspend fun deleteTask(task: Task) {
        repository.deleteTask(task)
    }
}