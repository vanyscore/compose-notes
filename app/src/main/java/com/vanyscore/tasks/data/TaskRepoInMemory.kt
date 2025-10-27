package com.vanyscore.tasks.data

import com.vanyscore.app.utils.DateUtils
import java.util.Calendar
import java.util.Date

class TaskRepoInMemory : ITaskRepo {

    private var _id = 0
    private val _tasks = mutableListOf<Task>()

    init {
        initializeTasks()
    }

    private fun initializeTasks() {
        val calendar = Calendar.getInstance()
        val today = calendar.time

        // Today
        createTaskForDate(today, "Morning Workout", "30 minutes cardio and strength training")
        createTaskForDate(today, "Team Meeting", "Weekly sync at 10:00 AM in Conference Room B")
        createTaskForDate(today, "Project Deadline", "Submit final report for client review")
        createTaskForDate(today, "Grocery Shopping", "Buy milk, eggs, fruits, and vegetables")

        // Tomorrow
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrow = calendar.time
        createTaskForDate(tomorrow, "Dentist Appointment", "Check-up at 2:00 PM")
        createTaskForDate(tomorrow, "Lunch with Colleagues", "Team lunch at Italian restaurant")
        createTaskForDate(tomorrow, "Study Session", "Complete Kotlin coroutines tutorial")
        createTaskForDate(tomorrow, "Call Parents", "Weekly catch-up call")

        // Day after tomorrow
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val dayAfterTomorrow = calendar.time
        createTaskForDate(dayAfterTomorrow, "Client Presentation", "Prepare slides and demo for new project")
        createTaskForDate(dayAfterTomorrow, "Yoga Class", "Evening yoga session at 6:00 PM")
        createTaskForDate(dayAfterTomorrow, "Book Flight", "Research and book flights for vacation")
        createTaskForDate(dayAfterTomorrow, "Review Budget", "Update monthly expenses and savings")

        // 3 days from now
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val threeDaysFromNow = calendar.time
        createTaskForDate(threeDaysFromNow, "Car Maintenance", "Oil change and tire rotation at 9:00 AM")
        createTaskForDate(threeDaysFromNow, "Movie Night", "Watch new release with friends")
        createTaskForDate(threeDaysFromNow, "Clean Apartment", "Deep clean kitchen and bathroom")
        createTaskForDate(threeDaysFromNow, "Plan Weekend", "Research activities for the weekend")

        // 4 days from now
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val fourDaysFromNow = calendar.time
        createTaskForDate(fourDaysFromNow, "Birthday Party", "Friend's birthday celebration at 7:00 PM")
        createTaskForDate(fourDaysFromNow, "Haircut Appointment", "Salon visit at 11:00 AM")
        createTaskForDate(fourDaysFromNow, "Submit Expense Report", "Upload receipts and submit for reimbursement")
        createTaskForDate(fourDaysFromNow, "Read Book", "Finish current chapter of programming book")

        // 5 days from now
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val fiveDaysFromNow = calendar.time
        createTaskForDate(fiveDaysFromNow, "Weekend Brunch", "Family brunch at favorite cafe")
        createTaskForDate(fiveDaysFromNow, "Gardening", "Plant new flowers and water plants")
        createTaskForDate(fiveDaysFromNow, "Update Portfolio", "Add recent projects to online portfolio")
        createTaskForDate(fiveDaysFromNow, "Plan Next Week", "Set goals and schedule for upcoming week")
    }

    private fun createTaskForDate(date: Date, title: String, description: String = "") {
        _tasks.add(
            Task(
                id = ++_id,
                title = title,
                isSuccess = false,
                date = date
            )
        )
    }

    override suspend fun createTask(task: Task) {
        _tasks.add(task.copy(
            id = ++_id
        ))
    }

    private fun fillIfEmpty() {
        if (_tasks.isEmpty()) {
            repeat(10) { index ->
                _tasks.add(Task(
                    id = ++_id,
                    title = "Task $index",
                    isSuccess = false,
                    date = Calendar.getInstance().time,
                ))
            }
        }
    }

    override suspend fun getTasks(date: Date): List<Task> {
        fillIfEmpty()
        return _tasks.filter {
            DateUtils.isDateEqualsByDay(it.date, date)
        }
    }

    override suspend fun getTasks(startDate: Date, endDate: Date): List<Task> {
        fillIfEmpty()
        return _tasks.filter {
            it.date.after(startDate) && it.date.before(endDate)
        }
    }

    override suspend fun updateTask(task: Task): Boolean {
        val found = _tasks.firstOrNull {
            it.id == task.id
        }
        val index = _tasks.indexOf(found)
        if (index == -1) return false
        _tasks.removeAt(index)
        _tasks.add(index, task)
        return true
    }

    override suspend fun deleteTask(task: Task): Boolean {
        val found = _tasks.firstOrNull { it.id == task.id }
        if (found == null) return false
        val index = _tasks.indexOf(found)
        _tasks.removeAt(index)
        return true
    }
}