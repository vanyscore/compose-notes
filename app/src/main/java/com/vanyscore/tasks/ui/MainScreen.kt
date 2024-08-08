package com.vanyscore.tasks.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vanyscore.tasks.data.Task
import com.vanyscore.tasks.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    val state = viewModel.state.collectAsState().value
    val tasks = state.tasks
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
            date = state.date,
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
                DayPickerBar { date ->
                    viewModel.changeDate(date)
                }
            }
            items(tasks.size, key = { index ->
                tasks[index].id
            }) {index ->
                val task = tasks[index]
                TaskItem(
                    task,
                    onTaskChanged = { updated ->
                        viewModel.taskStatusUpdated(updated)
                    },
                    onTaskEdit = { editTask ->
                        dialogState.value = true
                        editTaskState.value = editTask
                    },
                    onTaskDelete = { deleteTask ->
                        viewModel.deleteTask(deleteTask)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthAndYearPickerBar() {
    return TopAppBar(
        title = { Text("Список задач", style = MaterialTheme.typography.titleLarge.copy(
            color = Color.White,
        )) },
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun DayPickerBar(
    onDaySelected: (Date) -> Unit
) {
    val viewModel: MainViewModel = viewModel()
    val state = viewModel.state.collectAsState()
    val dates = remember {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR, 0)
            set(Calendar.MINUTE, 0)
        }
        val startDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH)
        val endDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val list = mutableListOf<Date>()
        startDay.until(endDay + 1).map { day ->
            val dateToAdd = calendar.let {
                it.set(Calendar.DAY_OF_MONTH, day)
                it.time.clone() as Date
            }
            list.add(dateToAdd)
        }
        list
    }

    val dateFormat = remember {
        SimpleDateFormat("E", Locale.getDefault())
    }

    return LazyRow {
        items(dates.size) {
            val date = dates[it]
            val calendar = Calendar.getInstance()
            calendar.time = date

            Box(
                modifier = Modifier
                    .clickable {
                        onDaySelected(date)
                    }
                    .background(
                        if (DateUtils.compareByDay(date, state.value.date))
                            MaterialTheme.colorScheme.inversePrimary
                        else MaterialTheme.colorScheme.primary
                    )
                    .size(52.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        calendar.get(Calendar.DAY_OF_MONTH).toString(),
                        style = TextStyle(
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        dateFormat.format(date),
                        style = TextStyle(
                            color = Color.White
                        )
                    )
                }
            }
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