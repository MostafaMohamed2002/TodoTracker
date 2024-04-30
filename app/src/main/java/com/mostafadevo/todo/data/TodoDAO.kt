package com.mostafadevo.todo.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mostafadevo.todo.data.model.Todo

@Dao
interface TodoDAO {
    @Query("select * from todo_table")
    fun getAllTodos():LiveData<List<Todo>>

    @Insert
    fun insertTodo(todo: Todo)

    //delete all todos
    @Query("delete from todo_table")
    fun deleteAllTodos()

    @Update
    suspend fun updateTodo(todo: Todo)

}