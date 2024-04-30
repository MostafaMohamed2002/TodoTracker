package com.mostafadevo.todo

import com.mostafadevo.todo.data.model.Priority

object Utils {
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
}