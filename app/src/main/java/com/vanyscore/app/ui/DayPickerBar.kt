package com.vanyscore.app.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.vanyscore.app.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class DayStatus {
    EMPTY, HAS_TASKS, IS_SUCCESS
}

@Composable
fun DayPickerBar(
    days: List<Date>,
    selectedDate: Date,
    checkDayStatus: (Date) -> DayStatus,
    onDaySelected: (Date) -> Unit,
) {
    var initialIndex = days.indexOfFirst {
        DateUtils.isCurrentDay(it)
    }
    if (initialIndex - 5 > 0) {
        initialIndex -= 5
    }
    if (initialIndex == -1) {
        initialIndex = 0
    }
    val rowState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val lastDatePicked = remember {
        mutableStateOf(selectedDate)
    }
    val selectedIndex = days.indexOfFirst {
        DateUtils.isDateEqualsByDay(selectedDate, it)
    }
    val scrollIndex = if (selectedIndex - 2 >= 0) selectedIndex - 2 else selectedIndex
    LaunchedEffect(key1 = selectedDate) {
        rowState.animateScrollToItem(scrollIndex)
    }
    lastDatePicked.value = selectedDate

    return LazyRow(
        state = rowState
    ) {
        items(days.size) {
            val date = days[it]
            val dayStatus = checkDayStatus(date)

            DayItem(
                dayStatus = dayStatus,
                date = date,
                onDaySelected = onDaySelected,
                isSelected = it == selectedIndex
            )
        }
    }
}

@Composable
fun DayItem(
    dayStatus: DayStatus = DayStatus.EMPTY,
    onDaySelected: (Date) -> Unit,
    date: Date,
    isSelected: Boolean
) {
    val calendar = Calendar.getInstance().apply {
        time = date
    }
    val dateFormat = remember {
        SimpleDateFormat("E", Locale.getDefault())
    }
    val backgroundColor = if (isSelected)
        MaterialTheme.colorScheme.inversePrimary
    else if (dayStatus == DayStatus.HAS_TASKS)
        MaterialTheme.colorScheme.tertiary
    else if (dayStatus == DayStatus.IS_SUCCESS)
        Color(0xFF008A10)
    else MaterialTheme.colorScheme.primary
    Box(
        modifier = Modifier
            .clickable {
                onDaySelected(date)
            }
            .background(
                backgroundColor
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