package com.vanyscore.tasks.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vanyscore.app.AppState
import com.vanyscore.app.ui.DayPickerBar
import com.vanyscore.app.ui.DayStatus
import com.vanyscore.app.utils.DateUtils
import com.vanyscore.tasks.R
import com.vanyscore.tasks.data.Task
import com.vanyscore.tasks.ui.dialogs.EditTaskDialog
import com.vanyscore.tasks.viewmodel.TaskViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TasksPage() {
    val viewModel = hiltViewModel<TaskViewModel>()
    val state = viewModel.state.collectAsState().value
    val tasks = state.monthTasks
    val currentDayTasks = tasks.filter {
        DateUtils.isDateEqualsByDay(it.date, state.selectedDate)
    }

    val dialogState = remember {
        mutableStateOf(false)
    }
    val editTaskState = remember {
        mutableStateOf<Task?>(null)
    }
    fun closeDialog() {
        dialogState.value = false
        editTaskState.value = null
    }
    if (dialogState.value) {
        val isEdit = editTaskState.value != null
        EditTaskDialog(
            date = state.selectedDate,
            task = editTaskState.value, onResult = { task ->
                closeDialog()
                if (!isEdit) {
                    viewModel.createTask(task)
                } else {
                    viewModel.editTask(task)
                }
            }) {
            closeDialog()
        }
    }

    return Scaffold(
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
        Column {
            DayPickerBar(
                state.monthDates,
                state.selectedDate,
                checkDayStatus = { day ->
                    val dayTasks = state.monthTasks.filter {
                        DateUtils.isDateEqualsByDay(it.date, day)
                    }
                    val isAllSuccess = dayTasks.all { t ->
                        t.isSuccess
                    }
                    if (dayTasks.isEmpty()) DayStatus.EMPTY
                    else {
                        if (isAllSuccess) DayStatus.IS_SUCCESS
                        else DayStatus.HAS_TASKS
                    }
                }
            ) { date ->
                AppState.updateState(AppState.source.value.copy(
                    date = date
                ))
            }
            if (currentDayTasks.isNotEmpty()) {
                TasksList(
                    tasks = currentDayTasks,
                    onTaskEdit = {editTask ->
                        dialogState.value = true
                        editTaskState.value = editTask
                    }
                )
            } else {
                EmptyTasks()
            }
        }
    }
}

@Composable
fun TasksList(
    tasks: List<Task>,
    onTaskEdit: (task: Task) -> Unit
) {
    val viewModel: TaskViewModel = hiltViewModel()
    val state = rememberLazyListState()
    return LazyColumn(
        state = state,
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(tasks.size, key = { index ->
            tasks[index].id
        }) { index ->
            val task = tasks[index]
            TaskItem(
                task,
                onTaskChanged = { updated ->
                    viewModel.taskStatusUpdated(updated)
                },
                onTaskEdit = onTaskEdit,
                onTaskDelete = { deleteTask ->
                    viewModel.deleteTask(deleteTask)
                }
            )
        }
    }
}



@Composable
fun EmptyTasks() {
    return Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Filled.List, "list",
                modifier = Modifier.size(112.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(stringResource(R.string.tasks_empty))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    task: Task,
    onTaskChanged: (Task) -> Unit,
    onTaskEdit: (Task) -> Unit,
    onTaskDelete: (Task) -> Unit
) {
    val swipeState = rememberDismissState(
        initialValue = DismissValue.Default,
        confirmValueChange = { value ->
            if (value == DismissValue.DismissedToEnd) {
                onTaskDelete(task)
            }
            true
        }
    )
    return SwipeToDismiss(
        state = swipeState,
        background = {
            Box(
                modifier = Modifier
                    .background(Color.Red)
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Filled.Delete,
                        modifier = Modifier
                            .padding(start = 16.dp),
                        contentDescription = "Delete",
                        tint = Color.White
                    )
                }
            }
        },
        dismissContent = {
            Box(modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {},
                    onLongClick = {
                        onTaskEdit(task)
                    }
                )) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
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
        },
        directions = setOf(DismissDirection.StartToEnd)
    )
}