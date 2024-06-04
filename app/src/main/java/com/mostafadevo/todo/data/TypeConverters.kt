package com.mostafadevo.todo.data

import androidx.room.TypeConverter
import com.mostafadevo.todo.data.model.Priority
import java.util.Date

class Converters {
    @TypeConverter
    fun fromPriority(priority: Priority):String{
        return priority.name
    }

    @TypeConverter
    fun toPriority(string: String): Priority {
        return Priority.valueOf(string)
    }

    // Tue Jun 04 14:16:57 GMT+03:00 2024 to Long
    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }

    // Long to Tue Jun 04 14:16:57 GMT+03:00 2024
    @TypeConverter
    fun toDate(time: Long): Date {
        return Date(time)
    }
}