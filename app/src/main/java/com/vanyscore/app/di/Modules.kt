package com.vanyscore.app.di

import android.content.Context
import androidx.room.Room
import com.vanyscore.app.room.AppDatabase
import com.vanyscore.notes.data.INoteRepo
import com.vanyscore.notes.data.NoteRepoRoom
import com.vanyscore.tasks.data.ITaskRepo
import com.vanyscore.tasks.data.TaskRepoRoom
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun database(
        @ApplicationContext
        context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context = context,
            AppDatabase::class.java, "tn_database"
        ).build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
class Modules {
    @Provides
    fun noteRepo(
        @ApplicationContext
        context: Context,
        database: AppDatabase,
    ): INoteRepo {
        return NoteRepoRoom(
            dao = database.notesDao(),
            contentResolver = context.contentResolver,
            outputImagesDir = File(context.filesDir, "/note_images/"),
            cacheDir = context.cacheDir,
        )
    }

    @Provides
    fun tasksRepo(database: AppDatabase): ITaskRepo {
        return TaskRepoRoom(
            database.tasksDao()
        )
    }
}