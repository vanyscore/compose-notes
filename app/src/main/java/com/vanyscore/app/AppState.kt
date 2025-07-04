package com.vanyscore.app

import com.vanyscore.app.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar
import java.util.Date

data class AppState(
    val date: Date,
    val theme: AppTheme = AppTheme.YELLOW_LIGHT,
) {
    companion object {
        private var _state = MutableStateFlow(AppState(date = Calendar.getInstance().time))
        val source = _state.asStateFlow()

        fun updateState(state: AppState) {
            _state.value = state
        }
    }
}