package com.vanyscore.app.utils

import java.util.Calendar
import java.util.Date

object DateUtils {

    fun isCurrentDay(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        return isDateEqualsByDay(calendar.time, date)
    }
    fun isDateEqualsByDay(date1: Date, date2: Date): Boolean {
        val c1 = Calendar.getInstance().apply {
            time = date1
        }
        val c2 = Calendar.getInstance().apply {
            time = date2
        }
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)
    }
}