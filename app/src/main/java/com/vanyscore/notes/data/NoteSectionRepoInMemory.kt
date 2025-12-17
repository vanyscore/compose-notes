package com.vanyscore.notes.data

import com.vanyscore.notes.domain.NoteSection
import java.util.Calendar

class NoteSectionRepoInMemory() : INoteSectionRepo {

    val noteSections = mutableListOf<NoteSection>()

    override suspend fun getNoteSections(): List<NoteSection> {
        return mutableListOf<NoteSection>().apply {
            addAll(noteSections)
        }
    }

    override suspend fun createNoteSection(name: String): NoteSection {
        val newSection = NoteSection(noteSections.size + 1, name = name,
            createdDate = Calendar.getInstance().time,
            updatedDate = Calendar.getInstance().time
        )
        noteSections.add(newSection)
        return newSection
    }

    override suspend fun editNoteSection(noteSection: NoteSection): NoteSection {
        val foundSection = noteSections.firstOrNull {
            it.id == noteSection.id
        }
        val index = noteSections.indexOf(foundSection)
        noteSections.removeAt(index)
        noteSections.add(index, noteSection)
        return noteSection
    }

    override suspend fun deleteNoteSection(id: Int) {
        noteSections.removeIf {
            it.id == id
        }
    }
}