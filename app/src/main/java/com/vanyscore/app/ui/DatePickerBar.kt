package com.vanyscore.app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.vanyscore.app.navigation.LocalMainNavController
import com.vanyscore.app.navigation.openSettings
import com.vanyscore.app.viewmodel.LocalAppViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerBar(
    datesSelectedChecker: DatesSelectedChecker? = null,
) {
    val dialogState = remember {
        mutableStateOf(false)
    }
    return TopAppBar(
        navigationIcon = {
            SettingsButton(
                isVisible = false
            )
        },
        title = {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .clickable {
                        dialogState.value = true
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DateControl()
                }
            }
            if (dialogState.value) {
                DatePickerDialogBuild(dialogState, datesSelectedChecker = datesSelectedChecker)
            }
        },
        actions = {
            SettingsButton()
        },
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun SettingsButton(isVisible: Boolean = true) {
    val navController = LocalMainNavController.current
    IconButton(
        modifier = Modifier.alpha(if (isVisible) 1f else 0f),
        onClick = {
            navController.openSettings()
        }
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            tint = MaterialTheme.colorScheme.onPrimary,
            contentDescription = "Settings" // Accessibility label
        )
    }
}

@Composable
fun DatePickerDialogBuild(dialogState: MutableState<Boolean>, datesSelectedChecker: DatesSelectedChecker? = null) {
    val appViewModel = LocalAppViewModel.current
    val appState = appViewModel.state
    val state = appState.collectAsState()
    Dialog(onDismissRequest = {
        dialogState.value = false
    }) {
        AppDatePicker(
            initDateMillis = state.value.date.time,
            datesSelectedChecker,
            onSelect = { time ->
                dialogState.value = false
                appViewModel.updateDate(Calendar.getInstance().apply {
                    timeInMillis = time
                }.time)
            })
    }
}

@Composable
fun DateControl() {
    val appViewModel = LocalAppViewModel.current
    val state = appViewModel.state.collectAsState()
    val currentDate = state.value.date
    val textStyle = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimary)
    val yearFormat = remember {
        SimpleDateFormat("yyyy", Locale.getDefault())
    }
    val monthFormat = remember {
        SimpleDateFormat("MMMM", Locale.getDefault())
    }
    val dayFormat = remember {
        SimpleDateFormat("dd", Locale.getDefault())
    }
    PrevDateButton()
    Text(dayFormat.format(currentDate), style = textStyle)
    Spacer(modifier = Modifier.width(8.dp))
    Text(monthFormat.format(currentDate), style = textStyle)
    Spacer(modifier = Modifier.width(8.dp))
    Text(yearFormat.format(currentDate), style = textStyle)
    NextDateButton()
}

@Composable
private fun PrevDateButton() {
    val appViewModel = LocalAppViewModel.current
    val appState = appViewModel.state.collectAsState()
    val currentDate = appState.value.date
    IconButton(
        onClick = {
            appViewModel.updateDate(Calendar.getInstance().apply {
                time = currentDate
                add(Calendar.DAY_OF_MONTH, -1)
            }.time)
        },
    ) {
        Icon(
            Icons.Default.KeyboardArrowLeft, "Left", tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun NextDateButton() {
    val appViewModel = LocalAppViewModel.current
    val appState = appViewModel.state.collectAsState()
    val state = appState.value
    val currentDate = appState.value.date
    IconButton(
        onClick = {
            appViewModel.updateDate(Calendar.getInstance().apply {
                time = currentDate
                add(Calendar.DAY_OF_MONTH, 1)
            }.time)
        }
    ) {
        Icon(
            Icons.Default.KeyboardArrowRight, "Right", tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}
