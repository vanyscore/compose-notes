package com.vanyscore.notes.data

import android.net.Uri
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import com.vanyscore.app.room.converters.DateConverter
import com.vanyscore.notes.domain.Note
import com.vanyscore.notes.domain.NoteImage
import java.util.Date

@Entity(tableName = "notes")
data class NoteRoom (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val description: String,
    @TypeConverters(value = [DateConverter::class])
    val created: Date,
    @TypeConverters(value = [DateConverter::class])
    val edited: Date
)

fun NoteRoom.toDomain(images: List<Uri> = emptyList()): Note {
    return Note(
        id = id,
        title = title,
        description = description,
        created = created,
        edited = edited,
        images = images.map {
            NoteImage(it, isTemporary = false)
        }
    )
}

fun Note.toRoom(): NoteRoom {
    return NoteRoom(
        id = id,
        title = title,
        description = description,
        created = created,
        edited = edited
    )
}

@Entity(tableName = "note_images")
data class NoteImageRoom(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val noteId: Int,
    val path: String
)

@Dao
interface NotesDao {
    @Insert
    suspend fun createNote(note: NoteRoom): Long
    @Query("SELECT * FROM notes WHERE created > (:fromDate) & created < (:endDate)")
    suspend fun getNotes(fromDate: Date, endDate: Date): List<NoteRoom>
    @Query("SELECT * FROM notes WHERE id = (:id)")
    suspend fun getNote(id: Int): List<NoteRoom>
    @Update
    suspend fun updateNote(note: NoteRoom)
    @Delete
    suspend fun deleteNote(note: NoteRoom)
    @Insert
    suspend fun createImage(noteImage: NoteImageRoom)
    @Query("SELECT * FROM note_images WHERE noteId = (:noteId)")
    suspend fun getImagesByNote(noteId: Int): List<NoteImageRoom>
    @Query("SELECT * FROM note_images WHERE path = (:path)")
    suspend fun getImageByPath(path: String): List<NoteImageRoom>
    @Delete
    suspend fun deleteImage(noteImage: NoteImageRoom)
}

suspend fun List<NoteRoom>.withImages(dao: NotesDao): List<Note> {
    return mapNotNull {
        val noteId = it.id
        if (noteId != null) {
            val images = dao.getImagesByNote(noteId).map { image ->
                Uri.parse(image.path)
            }
            it.toDomain(images = images)
        } else {
            null
        }
    }
}