package com.vanyscore.notes.data

import com.vanyscore.notes.domain.NoteSection

interface INoteSectionRepo {
    suspend fun getNoteSections(): List<NoteSection>
    suspend fun createNoteSection(name: String): NoteSection
    suspend fun editNoteSection(noteSection: NoteSection): NoteSection
    suspend fun deleteNoteSection(id: Int)
}

