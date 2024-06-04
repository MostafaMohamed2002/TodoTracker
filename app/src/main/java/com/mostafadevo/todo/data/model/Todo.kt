package com.mostafadevo.todo.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@kotlinx.parcelize.Parcelize
@Entity(tableName = "todo_table")
data class Todo(
    @PrimaryKey()
    var id: String,
    var title: String,
    var priority: Priority,
    var description: String,
    @ColumnInfo(defaultValue = false.toString())
    var isCompleted: Boolean

) : Parcelable {
    constructor() : this("", "", Priority.HIGH, "", false)
}