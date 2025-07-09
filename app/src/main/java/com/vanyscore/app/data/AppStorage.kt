package com.vanyscore.app.data

import android.content.Context
import com.vanyscore.app.theme.AppTheme
import com.vanyscore.app.theme.AppThemeType

interface IAppStorage {
    fun getTheme(): AppTheme
    fun setTheme(theme: AppTheme)
    fun getThemeType(): AppThemeType
    fun setThemeType(themeType: AppThemeType)
}

private const val APP_STORAGE_NAME = "APP_STORAGE"
private const val APP_STORAGE_THEME_KEY = "APP_THEME"
private const val APP_STORAGE_THEME_TYPE_KEY = "APP_THEME_TYPE"

class AppStorage(private val context: Context) : IAppStorage {
    override fun getTheme(): AppTheme {
        val prefs = context.getSharedPreferences(APP_STORAGE_NAME, Context.MODE_PRIVATE)
        val theme = prefs.getString(APP_STORAGE_THEME_KEY, null)
        return if (theme != null) AppTheme.valueOf(theme) else AppTheme.YELLOW
    }

    override fun setTheme(theme: AppTheme) {
        val prefs = context.getSharedPreferences(APP_STORAGE_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(APP_STORAGE_THEME_KEY, theme.name).apply()
    }

    override fun getThemeType(): AppThemeType {
        val prefs = context.getSharedPreferences(APP_STORAGE_NAME, Context.MODE_PRIVATE)
        val themeType = prefs.getString(APP_STORAGE_THEME_TYPE_KEY, null) ?: return AppThemeType.LIGHT
        return AppThemeType.valueOf(themeType)
    }

    override fun setThemeType(themeType: AppThemeType) {
        val prefs = context.getSharedPreferences(APP_STORAGE_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(APP_STORAGE_THEME_TYPE_KEY, themeType.name).apply()
    }
}