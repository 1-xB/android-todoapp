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
    ).addMigrations(AppDatabase.MIGRATION_1_2).build()


    private val taskDao = db.taskDao()

    suspend fun getAllTasks(): List<Task> {
        return taskDao.getAllTasks()
    }

    suspend fun getTaskById(taskId: Int): Task? {
        return taskDao.getTaskById(taskId)
    }

    suspend fun addTask(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun updateTaskCompletion(taskId: Int, isCompleted: Boolean) {
        taskDao.updateTaskCompletion(taskId, isCompleted)
    }

    suspend fun deleteTask(taskId: Int) {
        taskDao.deleteTask(taskId)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }
}