package com.mostafadevo.todo

import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import com.mostafadevo.todo.data.model.Priority
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

object Utils {
    const val CHANNEL_ID = "todo_channel"
    const val CHANNEL_NAME = "Todo Reminder"
    const val CHANNEL_DESCRIPTION = "channel for todo reminders"
    const val FIREBASE_TODO_COLLECTION_NAME = "todos"
    const val FIREBASE_USER_COLLECTION_NAME = "users"

    const val FIREBASE_USER_NAME_FIELD = "name"
    const val FIREBASE_USER_EMAIL_FIELD = "email"
    const val FIREBASE_USER_IMAGE_URL_FIELD = "imageUrl"

    const val SHARED_PREF_NAME = "todo_pref"
    const val FIREBASE_SYNC_ENABLED = "firebase_sync_enabled"

    fun parsePriorityToInt(priority: Priority): Int {
        return when (priority) {
            Priority.HIGH -> 0
            Priority.MEDIUM -> 1
            Priority.LOW -> 2
        }
    }

    fun parsePriorityFromStringToEnum(priority: String): Priority {
        return when (priority) {
            "High" -> Priority.HIGH
            "Medium" -> Priority.MEDIUM
            "Low" -> Priority.LOW
            else -> {
                Priority.LOW
            }
        }
    }

    fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validatePassword(password: String): Boolean {
        return password.length >= 6
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(date: Date): String {
        val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return localDate.format(formatter)
    }

    fun formatTime(date: Date): String {
        val formattedHours = if (date.hours < 10) "0${date.hours}" else "${date.hours}"
        val formattedMinutes = if (date.minutes < 10) "0${date.minutes}" else "${date.minutes}"
        return "$formattedHours:$formattedMinutes"
    }
}