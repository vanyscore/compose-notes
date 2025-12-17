package com.vanyscore.notes.data

import androidx.compose.runtime.mutableStateListOf
import com.vanyscore.notes.domain.Note
import com.vanyscore.notes.domain.NoteSection
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryInitializer @Inject constructor(
    val noteRepo: INoteRepo,
    val noteSectionRepo: INoteSectionRepo
) {

    init {
        initialize()
    }

    private var _id = 0

    fun initialize() {

        if (noteSectionRepo !is NoteSectionRepoInMemory) return
        noteSectionRepo.noteSections.addAll((mutableStateListOf(
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
            NoteSection(index + 1, title,
                createdDate = Calendar.getInstance().time,
                updatedDate = Calendar.getInstance().time
            )
        }.toMutableList()))

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
        if (noteRepo !is NoteRepoInMemory) return
        val notes = noteRepo.notes
        val currentTime = Calendar.getInstance().time
        notes.add(
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
}