package com.vanyscore.app.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vanyscore.app.room.converters.DateConverter
import com.vanyscore.notes.data.NoteImageRoom
import com.vanyscore.notes.data.NoteRoom
import com.vanyscore.notes.data.NotesDao
import com.vanyscore.tasks.data.TaskRoom
import com.vanyscore.tasks.data.TasksDao

@Database(entities = [TaskRoom::class, NoteRoom::class, NoteImageRoom::class], version = 1)
@TypeConverters(value = [DateConverter::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun tasksDao(): TasksDao
    abstract fun notesDao(): NotesDao
}