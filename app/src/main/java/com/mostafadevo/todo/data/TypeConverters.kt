package com.mostafadevo.todo.data

import androidx.room.TypeConverter
import com.mostafadevo.todo.data.model.Priority

class Converters {
    @TypeConverter
    fun fromPriority(priority: Priority):String{
        return priority.name
    }

    @TypeConverter
    fun toPriority(string: String): Priority {
        return Priority.valueOf(string)
    }
}