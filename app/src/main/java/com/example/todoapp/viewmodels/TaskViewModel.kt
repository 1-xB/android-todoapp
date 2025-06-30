package com.example.todoapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.todoapp.repository.TaskRepository

class TaskViewModel : ViewModel() {
    private val _taskRepository = TaskRepository()
}