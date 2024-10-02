package com.vanyscore.app

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vanyscore.notes.NotesPage
import com.vanyscore.tasks.ui.MonthAndYearPickerBar
import com.vanyscore.tasks.ui.TasksPage

@Composable
fun MainScreen() {
    val selectedTab = remember {
        mutableIntStateOf(0)
    }
    Log.d("debug", "current tab: ${selectedTab.intValue}")
    return Scaffold(
        topBar = {
            MonthAndYearPickerBar()
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column {
                TabRow(
                    selectedTabIndex = selectedTab.intValue,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary,
                    indicator = { tabs ->
                        if (selectedTab.intValue < 2) {
                            TabRowDefaults.Indicator(
                                Modifier.tabIndicatorOffset(tabs[selectedTab.intValue]),
                                color = MaterialTheme.colorScheme.onPrimary,
                                height = 3.dp
                            )
                        }
                    }
                ) {
                    Tab(
                        selectedTab.intValue == 0,
                        text = { Text("Заметки") },
                        onClick = {
                            selectedTab.intValue = 0
                        }
                    )
                    Tab(
                        selectedTab.intValue == 1,
                        text = { Text("Задачи") },
                        onClick = {
                            selectedTab.intValue = 1
                        }
                    )
                }
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (selectedTab.intValue == 0) {
                        NotesPage()
                    } else {
                        TasksPage()
                    }
                }
            }
        }
    }
}