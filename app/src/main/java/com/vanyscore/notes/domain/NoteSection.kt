package com.vanyscore.notes.domain

import com.vanyscore.notes.data.NoteRoom
import com.vanyscore.notes.data.NoteSectionRoom

data class NoteSection(
    val id: Int,
    val name: String,
)

fun NoteSectionRoom.toDomain(): NoteSection? {
    if (this.id == null) return null
    return NoteSection(
        id = this.id,
        name = this.name
    )
}

fun NoteSection.toRoom(): NoteSectionRoom {
    return NoteSectionRoom(
        id = this.id,
        name = this.name
    )
}