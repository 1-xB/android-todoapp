package com.example.todoapp.repository

import android.content.Context
import androidx.room.Room
import com.example.todoapp.data.AppDatabase
import com.example.todoapp.data.Task

class TaskRepository(context: Context) {
    private val db = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "todo_db"
    ).build()

    private val taskDao = db.taskDao()

    suspend fun getAllTasks(): List<Task> {
        return taskDao.getAllTasks()
    }

    suspend fun addTask(task: Task) {
        taskDao.insertTask(task)
    }
}