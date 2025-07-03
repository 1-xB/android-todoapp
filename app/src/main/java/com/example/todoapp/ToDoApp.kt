package com.example.todoapp

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todoapp.data.Task
import com.example.todoapp.ui.ToDoEditTaskScreen
import com.example.todoapp.ui.ToDoScreen
import com.example.todoapp.viewmodels.TaskViewModel
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ToDoApp(
    modifier: Modifier,
    taskViewModel: TaskViewModel
) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = "task_list",
        modifier = modifier
    ) {
        composable("task_list") {
            ToDoScreen(
                taskViewModel = taskViewModel,
                onNavigateToEditTask = { taskId ->
                    navController.navigate("edit_task/$taskId")
                }
            )
        }

        composable(
            "edit_task/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: 0
            var task by remember { mutableStateOf<Task?>(null) }
            var isLoading by remember { mutableStateOf(true) }

            LaunchedEffect(taskId) {
                try {
                    task = taskViewModel.getTaskById(taskId)
                } catch (e: Exception) {
                    task = null
                } finally {
                    isLoading = false
                }
            }
            if (!isLoading) {
                ToDoEditTaskScreen(
                    task = task,
                    onTaskUpdated = { id, title, description, isCompleted ->
                        coroutineScope.launch {
                            taskViewModel.updateTask(
                                Task(
                                    id = id,
                                    title = title,
                                    description = description,
                                    isCompleted = isCompleted
                                )
                            )
                            navController.popBackStack()
                        }
                    },
                    onTaskDeleted = { id ->
                        coroutineScope.launch {
                            taskViewModel.deleteTask(id)
                            navController.navigate("task_list") {
                                popUpTo("task_list") { inclusive = true }
                            } // Powrót do listy zadań po usunięciu, i czyszczenie stosu nawigacji
                        }

                    },
                    onError = { navController.popBackStack() },
                    onBack = { navController.popBackStack() },
                )
            }

        }
    }

}