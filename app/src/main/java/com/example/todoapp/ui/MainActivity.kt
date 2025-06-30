package com.example.todoapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.ui.theme.ToDoAppTheme
import com.example.todoapp.viewmodels.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val taskViewModel: TaskViewModel = TaskViewModel(
                repository = TaskRepository(this@MainActivity)
            )
            ToDoAppTheme {
                Scaffold(modifier = Modifier.Companion.fillMaxSize()) { innerPadding ->
                    ToDoApp(modifier = Modifier.padding(innerPadding),
                        taskViewModel = taskViewModel
                    )
                }
            }
        }
    }
}