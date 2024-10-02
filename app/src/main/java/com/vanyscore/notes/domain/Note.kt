package com.vanyscore.notes.domain

import java.util.Date

data class Note(
    val id: Int,
    val title: String,
    val description: String,
    val created: Date,
    val edited: Date
)