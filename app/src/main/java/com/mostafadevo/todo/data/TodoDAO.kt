package com.mostafadevo.todo.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mostafadevo.todo.data.model.Todo

@Dao
interface TodoDAO {
    @Query("select * from todo_table WHERE deleted = 0")
    fun getAllTodos(): List<Todo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTodo(todo: Todo)

    //delete all todos
    @Query("update todo_table set deleted = 1")
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
    @Query("SELECT * FROM todo_table where deleted=0 ORDER BY CASE priority WHEN 'LOW' THEN 1 WHEN 'MEDIUM' THEN 2 WHEN 'HIGH' THEN 3 ELSE 4 END ASC")
    fun sortByLowPriority(): LiveData<List<Todo>>

    @Query("SELECT * FROM todo_table where deleted=0 ORDER BY CASE priority WHEN 'HIGH' THEN 1 WHEN 'MEDIUM' THEN 2 WHEN 'LOW' THEN 3 ELSE 4 END ASC")
    fun sortByHighPriority(): LiveData<List<Todo>>


    @Query("SELECT * FROM todo_table where deleted=0 ORDER BY title ASC")
    fun sortByTitleAZ(): LiveData<List<Todo>>

    @Query("SELECT * FROM todo_table where deleted=0 ORDER BY title DESC")
    fun sortByTitleZA(): LiveData<List<Todo>>

    @Query("update todo_table set deleted = 1 where id = :itemToDelete")
    fun deleteTodoItem(itemToDelete: String)

    @Query("SELECT * FROM TODO_TABLE WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' AND deleted = 0")
    fun search(query: String): LiveData<List<Todo>>
}