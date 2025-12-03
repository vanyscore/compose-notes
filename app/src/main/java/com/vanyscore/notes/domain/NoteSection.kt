package com.vanyscore.notes.domain

import com.vanyscore.notes.data.NoteRoom
import com.vanyscore.notes.data.NoteSectionRoom
import java.util.Date

data class NoteSection(
    val id: Int,
    val name: String,
    val createdDate: Date,
    val updatedDate: Date,
)

fun NoteSectionRoom.toDomain(): NoteSection? {
    val id = this.id
    val createdDate = this.createdDate
    val name = this.name
    val updatedDate = this.updatedDate
    if (id == null || createdDate == null || name == null || updatedDate == null) return null
    return NoteSection(
        id = id,
        name = name,
        createdDate = createdDate,
        updatedDate = updatedDate,
    )
}

fun NoteSection.toRoom(): NoteSectionRoom {
    return NoteSectionRoom(
        id = this.id,
        name = this.name,
        createdDate = this.createdDate,
        updatedDate = this.updatedDate,
    )
}