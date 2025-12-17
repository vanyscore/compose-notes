package com.vanyscore.notes.data

import android.content.ContentResolver
import com.vanyscore.notes.domain.NoteSection
import com.vanyscore.notes.domain.toDomain
import com.vanyscore.notes.domain.toRoom
import java.io.File
import java.util.Calendar

class NoteSectionRepo(
    private val noteSectionsDao: NoteSectionsDao,
) : INoteSectionRepo {
    override suspend fun getNoteSections(): List<NoteSection> {
        return noteSectionsDao.getNoteSections().mapNotNull {
            it.toDomain()
        }
    }

    override suspend fun createNoteSection(name: String): NoteSection {
        val section = NoteSectionRoom(
            name = name,
            createdDate = Calendar.getInstance().time,
            updatedDate = Calendar.getInstance().time
        )
        val id = noteSectionsDao.createNoteSection(section)
        val updatedSection = section.copy(
            id = id.toInt()
        ).toDomain()
        // TODO: Add error handling
        if (updatedSection == null) throw Exception("Error create note_section")
        return updatedSection
    }

    override suspend fun editNoteSection(noteSection: NoteSection): NoteSection {
        val room = noteSection.toRoom().copy(
            updatedDate = Calendar.getInstance().time
        )
        noteSectionsDao.updateNoteSection(room)
        return noteSection
    }

    override suspend fun deleteNoteSection(id: Int) {
        val room = NoteSectionRoom(id = id)
        noteSectionsDao.deleteNoteSection(room)
    }
}