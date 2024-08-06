package com.vanyscore.tasks.data

import java.util.Date

data class Task(
    val id: Int = 0,
    val title: String,
    val isSuccess: Boolean,
    val date: Date,
)