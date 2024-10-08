package com.vanyscore.notes.domain

import java.util.Calendar
import java.util.Date

data class Note(
    val id: Int? = null,
    val title: String = "",
    val description: String = "",
    val created: Date = Calendar.getInstance().time,
    val edited: Date = Calendar.getInstance().time
)