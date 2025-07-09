package com.vanyscore.app.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanyscore.app.data.IAppStorage
import com.vanyscore.app.theme.AppTheme
import com.vanyscore.app.theme.AppThemeType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

data class AppState(
    val date: Date,
    val theme: AppTheme = AppTheme.YELLOW,
    val themeType: AppThemeType = AppThemeType.LIGHT
)

val LocalAppViewModel = staticCompositionLocalOf<AppViewModel> {
    error("No appView model provides.")
}

@Composable
fun AppViewModelProvider(content: @Composable () -> Unit) {
    return CompositionLocalProvider(
        LocalAppViewModel provides hiltViewModel<AppViewModel>(),
        content
    )
}

@HiltViewModel
class AppViewModel @Inject constructor(
    private val appStorage: IAppStorage
) : ViewModel() {

    private val _state = MutableStateFlow(AppState(
        date = Date(),
        theme = appStorage.getTheme(),
        themeType = appStorage.getThemeType(),
    ))
    val state = _state.asStateFlow()

    fun setTheme(appTheme: AppTheme) {
        viewModelScope.launch {
            appStorage.setTheme(appTheme)
            _state.update {
                it.copy(theme = appTheme)
            }
        }
    }

    fun setThemeType(appThemeType: AppThemeType) {
        viewModelScope.launch {
            appStorage.setThemeType(appThemeType)
            _state.update {
                it.copy(
                    themeType = appThemeType
                )
            }
        }
    }

    fun updateDate(date: Date) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    date = date
                )
            }
        }
    }
}