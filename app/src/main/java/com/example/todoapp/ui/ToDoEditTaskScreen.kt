package com.example.todoapp.ui


import android.os.Build
import android.text.format.DateFormat
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.data.Task
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ToDoEditTaskScreen(
    task: Task?,
    onTaskUpdated: (Int, String, String, Boolean, Long?) -> Unit,
    onTaskDeleted: (Int) -> Unit,
    onError: (String) -> Unit = {},
    onBack: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {



        if (task == null) {
            onError("Task not found")
        } else  {
            EditTaskContent(
                task = task,
                onTaskUpdated = { id, title, description, isCompleted, notificationTime ->
                    onTaskUpdated(id, title, description, isCompleted, notificationTime)
                },
                onTaskDeleted = { id ->
                    onTaskDeleted(id)
                },
                onBack = onBack
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ToDoEditTaskScreenPreview() {
    ToDoEditTaskScreen(
        task = Task(
            id = 1,
            title = "Sample Task",
            description = "This is a sample task description.",
            isCompleted = false,
            notificationTime = 123456789L
        ),
        onTaskUpdated = { _, _, _, _, _ -> },
        onTaskDeleted = {},
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskContent(
    task: Task,
    onTaskUpdated: (Int, String, String, Boolean, Long?) -> Unit,
    onTaskDeleted: (Int) -> Unit,
    onBack: () -> Unit = {},
) {
    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf<String>(task.description ?: "") }
    var isCompleted by remember { mutableStateOf(task.isCompleted) }
    var notificationTime by remember { mutableStateOf(task.notificationTime) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var isNotificationEnabled by remember { mutableStateOf(task.notificationTime != null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Bar z przyciskiem powrotu
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = "Edit Task",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            IconButton(
                onClick = { showDeleteDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Task",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Status zadania
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isCompleted)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isCompleted,
                    onCheckedChange = { isCompleted = it }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = if (isCompleted) "Task is completed" else "Task in progress",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Pola edycji
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },

            minLines = 3,
            maxLines = 5,
            modifier = Modifier.fillMaxWidth()
        )

        // Pola daty i czasu
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Notification Time",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Switch(
                checked = isNotificationEnabled,
                onCheckedChange = {isNotificationEnabled = it },
            )
        }

        if (isNotificationEnabled) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = notificationTime?.let { "Set Notification" } ?: "No Notification Set",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select Date"
                    )
                }
            }

            Text(
                text = notificationTime?.let { time ->
                    val zoneId = ZoneId.systemDefault()
                    val dateTime = Instant.ofEpochMilli(time).atZone(zoneId).toLocalDateTime()
                    val is24Hour = DateFormat.is24HourFormat(LocalContext.current)
                    val timePattern = if (is24Hour) "HH:mm" else "hh:mm a"
                    dateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy, $timePattern"))
                } ?: "no time set",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }


        Spacer(modifier = Modifier.weight(1f))

        // Przyciski akcji
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel")
            }

            Button(
                onClick = {
                    onTaskUpdated(task.id, title, description, isCompleted, notificationTime)
                },
                modifier = Modifier.weight(1f),
                enabled = title.isNotBlank()
            ) {
                Text("Save")
            }
        }
    }

    // Dialog potwierdzenia usunięcia
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Task") },
            text = { Text("Are you sure you want to delete this task?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onTaskDeleted(task.id)
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        notificationTime = millis
                        showDatePicker = false

                        showTimePicker = true
                    }
                }) {
                    Text("Submit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = 12,
            initialMinute = 0,
            is24Hour = DateFormat.is24HourFormat(LocalContext.current)
        )



        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("Select Time") },
            text = {
                TimeInput(
                    state = timePickerState,
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        notificationTime?.let { dateMillis ->
                            val zoneId = ZoneId.systemDefault()
                            val selectedDate = Instant.ofEpochMilli(dateMillis).atZone(zoneId).toLocalDate()
                            val selectedTime = selectedDate.atTime(timePickerState.hour, timePickerState.minute)
                            notificationTime = selectedTime.atZone(zoneId).toInstant().toEpochMilli()
                        }
                        showTimePicker = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}