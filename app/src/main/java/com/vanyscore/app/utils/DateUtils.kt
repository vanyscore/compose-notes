package com.vanyscore.app.utils

import java.util.Calendar
import java.util.Date

object DateUtils {

    fun getAllDaysFromMonth(currentDate: Date): List<Date> {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = currentDate.time
        }.apply {
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
        return list
    }

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