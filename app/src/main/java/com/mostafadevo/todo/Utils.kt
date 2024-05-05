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

    fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validatePassword(password: String): Boolean {
        return password.length >= 6
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }


    fun validateSignUp(email: String, password: String, confirmPassword: String): Boolean {
        return validateEmail(email) && validatePassword(password) && validateConfirmPassword(
            password,
            confirmPassword
        )
    }

    fun validateSignIn(email: String, password: String): Boolean {
        return validateEmail(email) && validatePassword(password)
    }

}