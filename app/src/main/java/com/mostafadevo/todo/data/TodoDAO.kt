package com.mostafadevo.todo.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mostafadevo.todo.data.model.Todo

@Dao
interface TodoDAO {
    @Query("select * from todo_table")
    fun getAllTodos():LiveData<List<Todo>>

    @Insert
    fun insertTodo(todo: Todo)
}