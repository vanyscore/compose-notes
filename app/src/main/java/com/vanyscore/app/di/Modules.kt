package com.vanyscore.app.di

import android.content.Context
import androidx.room.Room
import com.vanyscore.app.data.AppStorage
import com.vanyscore.app.data.IAppStorage
import com.vanyscore.app.room.AppDatabase
import com.vanyscore.app.viewmodel.AppViewModel
import com.vanyscore.notes.data.INoteRepo
import com.vanyscore.notes.data.INoteSectionRepo
import com.vanyscore.notes.data.InMemoryInitializer
import com.vanyscore.notes.data.NoteRepoInMemory
import com.vanyscore.notes.data.NoteSectionRepoInMemory
import com.vanyscore.tasks.data.ITaskRepo
import com.vanyscore.tasks.data.TaskRepoInMemory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


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
            AppDatabase::class.java, name = "main"
        ).build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
class ViewModelModule {
    @Provides
    fun provideAppViewModel(
        storage: IAppStorage
    ): AppViewModel {
        return AppViewModel(appStorage = storage)
    }
}

@Module
@InstallIn(SingletonComponent::class)
class Modules {
    @Provides
    @Singleton
    fun noteRepo(
        @ApplicationContext
        context: Context,
        database: AppDatabase,
    ): INoteRepo {
        return NoteRepoInMemory(
            contentResolver = context.contentResolver,
            cacheDir = context.cacheDir,
        )
//        return NoteRepoRoom(
//            dao = database.notesDao(),
//            noteSectionsDao = database.noteSectionsDao(),
//            contentResolver = context.contentResolver,
//            outputImagesDir = File(context.filesDir, "/note_images/"),
//            cacheDir = context.cacheDir,
//        )
    }

    @Provides
    @Singleton
    fun noteSectionRepo(
        database: AppDatabase,
    ): INoteSectionRepo {
        return NoteSectionRepoInMemory()
//        return NoteSectionRepo(
//            noteSectionsDao = database.noteSectionsDao(),
//        )
    }

    @Provides
    @Singleton
    fun tasksRepo(database: AppDatabase): ITaskRepo {
        return TaskRepoInMemory()
//        return TaskRepoRoom(
//            database.tasksDao()
//        )
    }

    @Provides
    fun appStorage(
        @ApplicationContext
        context: Context
    ): IAppStorage {
        return AppStorage(context)
    }
}