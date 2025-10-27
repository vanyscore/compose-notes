package com.vanyscore.notes.data

import android.content.ContentResolver
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import com.vanyscore.app.domain.EventBus
import com.vanyscore.app.utils.DateUtils
import com.vanyscore.app.utils.FileUtil
import com.vanyscore.notes.domain.Note
import com.vanyscore.notes.domain.NoteImage
import com.vanyscore.notes.domain.NoteSection
import java.io.File
import java.util.Calendar
import java.util.Date
import java.util.UUID

class NoteRepoInMemory(
    private val contentResolver: ContentResolver,
    private val cacheDir: File,
) : INoteRepo {

    private var _id = 0
    private val _notes = mutableListOf<Note>()

    private val _noteSections = mutableStateListOf(
        "Meeting Notes",
        "Project Ideas",
        "Shopping List",
        "Travel Plans",
        "Learning Goals",
        "Daily Journal",
        "Recipe Collection",
        "Book Summaries",
        "Workout Routine",
        "Creative Writing"
    ).mapIndexed { index, title ->
        NoteSection(index + 1, title)
    }.toMutableList()

    init {
        // Initialize notes for each section
        initializeNotes()
    }

    private fun initializeNotes() {
        // Section 1: Meeting Notes
        createNoteForSection(1, "Weekly Team Sync", "Discuss project progress and blockers for the upcoming sprint.")
        createNoteForSection(1, "Client Meeting Notes", "Follow up on action items from client call.")
        createNoteForSection(1, "Product Review", "Key decisions from product design review meeting.")

        // Section 2: Project Ideas
        createNoteForSection(2, "Mobile App Concept", "Fitness tracking app with AI-powered workout recommendations.")
        createNoteForSection(2, "Browser Extension", "Productivity tool for managing multiple social media accounts.")
        createNoteForSection(2, "E-commerce Platform", "Niche marketplace for handmade crafts and artisanal products.")

        // Section 3: Shopping List
        createNoteForSection(3, "Grocery List", "Milk, eggs, bread, fruits, vegetables, chicken, rice.")
        createNoteForSection(3, "Electronics", "New headphones, phone charger, USB-C cable, power bank.")
        createNoteForSection(3, "Home Supplies", "Laundry detergent, cleaning supplies, light bulbs, batteries.")

        // Section 4: Travel Plans
        createNoteForSection(4, "Summer Vacation", "Research destinations: Bali, Thailand, or Greece for summer trip.")
        createNoteForSection(4, "Business Trip", "Prepare documents and schedule for upcoming conference in Berlin.")
        createNoteForSection(4, "Weekend Getaway", "Look for nearby mountain resorts for weekend hiking trip.")

        // Section 5: Learning Goals
        createNoteForSection(5, "Kotlin Mastery", "Complete advanced Kotlin coroutines and Flow tutorials.")
        createNoteForSection(5, "UI/UX Design", "Learn Figma prototyping and design system principles.")
        createNoteForSection(5, "Machine Learning", "Start with Python basics and TensorFlow introduction.")

        // Section 6: Daily Journal
        createNoteForSection(6, "Morning Reflection", "Today's focus: complete project documentation and team meeting.")
        createNoteForSection(6, "Evening Recap", "Accomplished: finished sprint planning, learned new Compose techniques.")
        createNoteForSection(6, "Weekly Review", "Progress on goals: good momentum on learning project, need better time management.")

        // Section 7: Recipe Collection
        createNoteForSection(7, "Pasta Carbonara", "Ingredients: spaghetti, eggs, pancetta, parmesan, black pepper.")
        createNoteForSection(7, "Vegetable Stir Fry", "Quick dinner: bell peppers, broccoli, carrots, tofu, soy sauce.")
        createNoteForSection(7, "Smoothie Recipes", "Green smoothie: spinach, banana, mango, almond milk, chia seeds.")

        // Section 8: Book Summaries
        createNoteForSection(8, "Atomic Habits", "Key takeaway: focus on systems rather than goals for lasting change.")
        createNoteForSection(8, "Deep Work", "Importance of uninterrupted focus time for meaningful work.")
        createNoteForSection(8, "The Psychology of Money", "Financial success is more about behavior than intelligence.")

        // Section 9: Workout Routine
        createNoteForSection(9, "Upper Body Day", "Push-ups, pull-ups, shoulder press, bicep curls, tricep extensions.")
        createNoteForSection(9, "Lower Body Day", "Squats, lunges, deadlifts, calf raises, leg press.")
        createNoteForSection(9, "Cardio Schedule", "Monday: running, Wednesday: cycling, Friday: swimming intervals.")

        // Section 10: Creative Writing
        createNoteForSection(10, "Short Story Idea", "Sci-fi concept: memory trading in a futuristic society.")
        createNoteForSection(10, "Poem Draft", "Working on a poem about urban life and nature's resilience.")
        createNoteForSection(10, "Character Development", "Protagonist background: former detective turned private investigator.")

        // Add some notes without sections
        createNoteForSection(null, "Random Thoughts", "Ideas and observations that don't fit into specific categories.")
        createNoteForSection(null, "Quick Reminders", "Important tasks and deadlines to keep track of.")
    }

    private fun createNoteForSection(sectionId: Int?, title: String, description: String) {
        val currentTime = Calendar.getInstance().time
        _notes.add(
            Note(
                id = ++_id,
                sectionId = sectionId,
                title = title,
                description = description,
                created = currentTime,
                edited = currentTime
            )
        )
    }

    override suspend fun getNoteSections(): List<NoteSection> {
        return mutableListOf<NoteSection>().apply {
            addAll(_noteSections)
        }
    }

    override suspend fun createNoteSection(name: String): NoteSection {
        val newSection = NoteSection(_noteSections.size + 1, name)
        _noteSections.add(newSection)
        return newSection
    }

    override suspend fun editNoteSection(noteSection: NoteSection): NoteSection {
        val foundSection = _noteSections.firstOrNull {
            it.id == noteSection.id
        }
        val index = _noteSections.indexOf(foundSection)
        _noteSections.removeAt(index)
        _noteSections.add(index, noteSection)
        return noteSection
    }

    override suspend fun deleteNoteSection(id: Int) {
        _noteSections.removeIf {
            it.id == id
        }
    }

    override suspend fun createNote(note: Note) {
        _notes.add(note.copy(
            id = ++_id
        ))
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
        // TODO: Доработать (from-to: Date).
        return _notes.filter {
            DateUtils.isDateEqualsByDay(it.created, fromDate)
        }
    }

    override suspend fun getNotes(sectionId: Int): List<Note> {
        return _notes.filter { it.sectionId == sectionId }
    }

    override suspend fun getNotes(date: Date): List<Note> {
        return _notes.filter { DateUtils.isDateEqualsByDay(it.created, date) }
    }

    override suspend fun getNote(id: Int): Note? {
        return _notes.firstOrNull {
            it.id == id
        }
    }

    override suspend fun updateNote(note: Note): Boolean {
        val found = _notes.firstOrNull {
            it.id == note.id
        }
        val index = _notes.indexOf(found)
        if (index == -1) return false
        _notes.removeAt(index)
        _notes.add(index, note)
        EventBus.triggerNotesUpdated()
        return true
    }

    override suspend fun deleteNote(note: Note): Boolean {
        val found = _notes.firstOrNull { it.id == note.id }
        if (found == null) return false
        val index = _notes.indexOf(found)
        _notes.removeAt(index)
        EventBus.triggerNotesUpdated()
        return true
    }
}