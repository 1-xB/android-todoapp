package com.example.todoapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.todoapp.data.Task
import com.example.todoapp.repository.TaskRepository

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    suspend fun getAllTasks() = repository.getAllTasks()

    suspend fun getTaskById(taskId: Int) = repository.getTaskById(taskId)

    suspend fun addTask(task: Task) {
        repository.addTask(task)
    }

    suspend fun updateTaskCompletion(taskId: Int, isCompleted: Boolean) {
        repository.updateTaskCompletion(taskId, isCompleted)
    }

    suspend fun deleteTask(taskId: Int) {
        repository.deleteTask(taskId)
    }

    suspend fun updateTask(task: Task) {
        repository.updateTask(task)
    }
}