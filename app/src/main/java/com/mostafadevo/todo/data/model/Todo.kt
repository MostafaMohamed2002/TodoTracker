package com.mostafadevo.todo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class Todo (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String,
    var priority: Priority,
    var description: String

)