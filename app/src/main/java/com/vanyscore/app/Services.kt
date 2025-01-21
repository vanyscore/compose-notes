package com.vanyscore.app

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import androidx.room.Room
import com.vanyscore.app.room.AppDatabase
import com.vanyscore.notes.data.INoteRepo
import com.vanyscore.notes.data.NoteRepoRoom
import com.vanyscore.tasks.data.ITaskRepo
import com.vanyscore.tasks.data.TaskRepoRoom
import java.io.File

// TODO: Move to DI(hilt).
object Services {
    val tasksRepo: ITaskRepo by lazy {
        TaskRepoRoom(
            database.tasksDao()
        )
    }
    val notesRepo: INoteRepo by lazy {
        NoteRepoRoom(database.notesDao())
    }

    private lateinit var applicationContext: Context
    lateinit var cacheDir: File
    lateinit var filesDir: File
    lateinit var noteImagesDir: File
    lateinit var contentResolver: ContentResolver


    private lateinit var database: AppDatabase

    fun bindApplicationContext(application: Application) {
        applicationContext = application.applicationContext
    }

    fun build() {
//        database = Room.inMemoryDatabaseBuilder(
//            context = appContext,
//            AppDatabase::class.java,
//        ).build()
        database = Room.databaseBuilder(
            context = applicationContext,
            AppDatabase::class.java, "tn_database"
        ).build()
        cacheDir = applicationContext.cacheDir
        filesDir = applicationContext.filesDir
        noteImagesDir = File(applicationContext.filesDir, "/note_images/")
        contentResolver = applicationContext.contentResolver

    }
}