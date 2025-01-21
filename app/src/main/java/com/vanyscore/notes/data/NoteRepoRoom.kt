package com.vanyscore.notes.data

import android.content.ContentResolver
import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.vanyscore.app.Services
import com.vanyscore.app.domain.EventBus
import com.vanyscore.app.utils.DateUtils
import com.vanyscore.app.utils.FileUtil
import com.vanyscore.app.utils.Logger
import com.vanyscore.notes.domain.Note
import com.vanyscore.notes.domain.NoteImage
import com.vanyscore.notes.domain.moveIfItTemporary
import java.io.File
import java.util.Date
import java.util.UUID

class NoteRepoRoom(
    private val dao: NotesDao,
    private val contentResolver: ContentResolver = Services.contentResolver,
    private val outputImagesDir: File = Services.noteImagesDir,
    private val cacheDir: File = Services.cacheDir,
) : INoteRepo {

    override suspend fun createNote(note: Note) {
        val noteId = dao.createNote(note.toRoom()).toInt()
        note.images.moveIfItTemporary(outputImagesDir).forEach {
            val path = it.uri.toString()
            dao.createImage(NoteImageRoom(null, noteId, path))
        }
        EventBus.triggerNotesUpdated()
    }

    override suspend fun attachImage(note: Note, uri: Uri): Note? {
        val attachmentUUID = UUID.randomUUID()
        val attachmentFileExtension = FileUtil.getFileExtensionFromUri(contentResolver, uri)
        val fileName = "${attachmentUUID}.$attachmentFileExtension"
        // Save image in cache dir.
        val savedFileUri = FileUtil
            .saveFileToInternalStorage(contentResolver, uri, fileName, dir = cacheDir) ?: return null
        return note.copy(
            images = note.images.toMutableList().apply {
                add(NoteImage(savedFileUri, isTemporary = true))
            }
        )
    }

    override suspend fun getNotes(fromDate: Date, toDate: Date): List<Note> {
        val notes = dao.getNotes(fromDate, toDate).withImages(dao)
        return notes.filter {
            DateUtils.isDateEqualsByDay(it.created, fromDate)
        }
    }

    override suspend fun getNote(id: Int): Note? {
        val note = dao.getNote(id).withImages(dao).firstOrNull()
        note?.images?.forEach {
            Logger.log("Image path: ${it.uri.toFile().path}")
        }
        return note
    }

    override suspend fun updateNote(note: Note): Boolean {
        if (note.id == null) return false
        val oldNote = getNote(note.id) ?: return false
        val oldImages = oldNote.images
        val actualImages = note.images
        if (actualImages.size > oldImages.size) {
            val newImages = mutableListOf<NoteImage>()
            actualImages.forEach {
                if (!oldImages.contains(it)) {
                    newImages.add(it)
                }
            }
            newImages.moveIfItTemporary(outputImagesDir).forEach {
                val uriPath = it.uri.toString()
                dao.createImage(NoteImageRoom(null, note.id, uriPath))
            }

        } else if (actualImages.size < oldImages.size) {
            val noteImagesToRemove = mutableListOf<NoteImage>()
            oldImages.forEach {
                if (!actualImages.contains(it)) {
                    noteImagesToRemove.add(it)
                }
            }
            noteImagesToRemove.mapNotNull {
                dao.getImageByPath(it.uri.toString()).firstOrNull()
            }.forEach {
                FileUtil.removeFileByUri(it.path.toUri())
                dao.deleteImage(it)
            }
        }
        dao.updateNote(note.toRoom())
        EventBus.triggerNotesUpdated()
        return true
    }

    override suspend fun deleteNote(note: Note): Boolean {
        val noteId = note.id ?: return false
        val images = dao.getImagesByNote(noteId)
        images.forEach {
            FileUtil.removeFileByUri(it.path.toUri())
            dao.deleteImage(it)
        }
        dao.deleteNote(note.toRoom())
        EventBus.triggerNotesUpdated()
        return true
    }
}