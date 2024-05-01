package com.mostafadevo.todo.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mostafadevo.todo.data.model.Todo

@Dao
interface TodoDAO {
    @Query("select * from todo_table")
    fun getAllTodos(): LiveData<List<Todo>>

    @Insert
    fun insertTodo(todo: Todo)

    //delete all todos
    @Query("delete from todo_table")
    fun deleteAllTodos()

    @Update
    fun updateTodo(todo: Todo)

    //handle sorting
    /*
        newest
        oldest
        priority high
        Priority low
        title a z
        title z a
    */
    @Query("SELECT * FROM todo_table ORDER BY CASE priority WHEN 'LOW' THEN 1 WHEN 'MEDIUM' THEN 2 WHEN 'HIGH' THEN 3 ELSE 4 END ASC")
    fun sortByLowPriority(): LiveData<List<Todo>>

    @Query("SELECT * FROM todo_table ORDER BY CASE priority WHEN 'HIGH' THEN 1 WHEN 'MEDIUM' THEN 2 WHEN 'LOW' THEN 3 ELSE 4 END ASC")
    fun sortByHighPriority(): LiveData<List<Todo>>

    @Query("SELECT * FROM todo_table ORDER BY id DESC")
    fun sortByNewest(): LiveData<List<Todo>>

    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun sortByOldest(): LiveData<List<Todo>>

    @Query("SELECT * FROM todo_table ORDER BY title ASC")
    fun sortByTitleAZ(): LiveData<List<Todo>>

    @Query("SELECT * FROM todo_table ORDER BY title DESC")
    fun sortByTitleZA(): LiveData<List<Todo>>

    @Delete
    fun deleteTodoItem(itemToDelete: Todo)

    @Query("SELECT * FROM TODO_TABLE WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun search(query: String): LiveData<List<Todo>>
}