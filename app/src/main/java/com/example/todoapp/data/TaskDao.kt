package com.example.todoapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskDao {
    @Query("SELECT * FROM Tasks ORDER BY createdAt DESC")
    suspend fun getAllTasks(): List<Task>

    @Insert
    suspend fun insertTask(task: Task)
}