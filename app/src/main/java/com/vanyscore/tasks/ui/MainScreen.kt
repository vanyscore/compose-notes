package com.vanyscore.tasks.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vanyscore.tasks.data.Task

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    val state = viewModel.state.collectAsState().value
    val tasks = state.tasks
    val dialogState = remember {
        mutableStateOf(false)
    }

    if (dialogState.value) {
        EditTaskDialog(task = null, onResult = { task ->
            viewModel.createTask(task)
            dialogState.value = false
        }) {
            dialogState.value = false
        }
    }

    return Scaffold(
        topBar = {
            MonthAndYearPickerBar()
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                dialogState.value = true
            }) {
                Icon(
                    Icons.Filled.Add,
                    "Add"
                )
            }
        }
    ) {
        LazyColumn(
            Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            item {
                DayPickerBar()
            }
            items(tasks.size) {index ->
                val task = tasks[index]
                TaskItem(
                    task,
                    onTaskChanged = { updated ->
                        viewModel.taskStatusUpdated(updated)
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthAndYearPickerBar() {
    return TopAppBar(title = { Text("Список задач") })
}

@Composable
fun DayPickerBar() {

}

@Composable
fun TaskItem(
    task: Task,
    onTaskChanged: (Task) -> Unit
) {
    return Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(task.title)
            Checkbox(checked = task.isSuccess, onCheckedChange = {
                onTaskChanged(task.copy(
                    isSuccess = it
                ))
            })
        }
    }
}