package com.vanyscore.app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanyscore.app.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun AppDatePicker(
    initDateMillis: Long? = Calendar.getInstance().timeInMillis,
    selectedMillis: List<Long> = emptyList(),
    onSelect: (Long) -> Unit
) {
    val currentDt = remember { mutableStateOf(Calendar.getInstance().apply {
        if (initDateMillis != null) {
            timeInMillis = initDateMillis
        }
    }.time) }
    val initDt = Calendar.getInstance().time

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.onPrimary
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            MonthSelect(dt = currentDt)
            Box(modifier = Modifier.padding(horizontal = 6.dp)) {
                Weekdays()
            }
            Spacer(modifier = Modifier.height(8.dp))
            Days(currentDt = currentDt, initDt = initDt, selectedMillis, onSelect = onSelect)
        }
    }
}

@Composable
fun Weekdays() {
    val calendar = Calendar.getInstance()
    val weekDates = mutableListOf<Date>()
    for (i: Int in 1..7) {
        weekDates.add(calendar.apply { set(Calendar.DAY_OF_WEEK, i) }.time)
    }
    val dateFormat = SimpleDateFormat("E", Locale.getDefault())
    return Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        weekDates.map { dt ->
            Text(dateFormat.format(dt).uppercase(Locale.getDefault()), style = TextStyle(
                fontSize = 16.sp
            ))
        }
    }
}

@Composable
fun MonthSelect(dt: MutableState<Date>) {
    val dtFormat = SimpleDateFormat("MMMM, yyyy", Locale.getDefault())
    return Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                dt.value = Calendar.getInstance().apply {
                    time = dt.value
                    set(Calendar.MONTH, get(Calendar.MONTH) - 1)
                }.time
            }
        ) {
            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "prevDate")
        }
        Text(dtFormat.format(dt.value), style = TextStyle(
            fontWeight = FontWeight.Bold
        ))
        IconButton(
            onClick = {
                dt.value = Calendar.getInstance().apply {
                    time = dt.value
                    set(Calendar.MONTH, get(Calendar.MONTH) + 1)
                }.time
            }
        ) {
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "nextDate")
        }
    }
}

@Composable
fun Days(currentDt: MutableState<Date>, initDt: Date, selectedMillis: List<Long>, onSelect: (Long) -> Unit) {
    val firstDt = Calendar.getInstance().apply {
        time = currentDt.value
    }
    while (firstDt.get(Calendar.DAY_OF_MONTH) != 1) {
        firstDt.set(Calendar.DAY_OF_YEAR, firstDt.get(Calendar.DAY_OF_YEAR) - 1)
    }
    while (firstDt.get(Calendar.DAY_OF_WEEK) != 1) {
        firstDt.set(Calendar.DAY_OF_YEAR, firstDt.get(Calendar.DAY_OF_YEAR) - 1)
    }
    val firstDtTime = firstDt.time
    val formatter = SimpleDateFormat("dd", Locale.getDefault())
    val selectedDates = selectedMillis.map {
        Calendar.getInstance().apply {
            timeInMillis = it
        }.time
    }
    return Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        for (i in 0 until 5) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (k in 1 ..7) {
                    val showDt = firstDt.apply {
                        time = firstDtTime
                        set(Calendar.DAY_OF_YEAR, get(Calendar.DAY_OF_YEAR) + (i * 7 + k))
                    }.time
                    val isCurrentDt = DateUtils.isDateEqualsByDay(showDt, initDt)
                    val isCurrentMonth = Calendar.getInstance().apply {
                        time = showDt
                    }.get(Calendar.MONTH) == Calendar.getInstance().apply {
                        time = currentDt.value
                    }.get(Calendar.MONTH)
                    var isSelected = false
                    selectedDates.forEach {  selDt ->
                        if (DateUtils.isDateEqualsByDay(selDt, showDt)) {
                            isSelected = true
                        }
                    }
                    val color = if (isCurrentDt) MaterialTheme.colorScheme.primary
                    else if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.surface
                    Surface(
                        modifier = Modifier
                            .padding(0.dp)
                            .clickable {
                                onSelect(showDt.time)
                            },
                        shape = CircleShape,
                        color = color
                    ) {
                        Box(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(formatter.format(showDt), style = TextStyle(
                                color = if (isCurrentMonth) Color.Unspecified else MaterialTheme.colorScheme.scrim
                            ))
                        }
                    }
                }
            }
        }
    }
}

