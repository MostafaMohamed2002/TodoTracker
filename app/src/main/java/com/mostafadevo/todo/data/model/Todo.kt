package com.mostafadevo.todo.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@kotlinx.parcelize.Parcelize
@Entity(tableName = "todo_table")
data class Todo(
    @PrimaryKey()
    var id: String,
    var title: String,
    var priority: Priority,
    var description: String

) : Parcelable {
    constructor() : this("", "", Priority.HIGH, "")
}