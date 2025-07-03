package com.example.todoapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {
    @Query("SELECT * FROM Tasks ORDER BY createdAt DESC")
    suspend fun getAllTasks(): List<Task>

    @Query("SELECT * FROM Tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): Task?

    @Insert
    suspend fun insertTask(task: Task)

    @Query("UPDATE Tasks SET isCompleted = :isCompleted WHERE id = :taskId")
    suspend fun updateTaskCompletion(taskId: Int, isCompleted: Boolean)

    @Query("DELETE FROM Tasks WHERE id = :taskId")
    suspend fun deleteTask(taskId: Int)

    @Update
    suspend fun updateTask(task: Task)
}