package com.mostafadevo.todo.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mostafadevo.todo.data.model.Todo

/**
 * Data Access Object for the todos table.
 */
@Dao
interface TodoDAO {
    /**
     * Get all todos from the table.
     * @return all todos.
     */
    @Query("select * from todo_table")
    fun getAllTodos():LiveData<List<Todo>>

    /**
     * Insert a todo in the database. If the todo already exists, replace it.
     * @param todo the todo to be inserted.
     */
    @Insert
    fun insertTodo(todo: Todo)

    /**
     * Delete all todos from the table.
     */
    @Query("delete from todo_table")
    fun deleteAllTodos()

    /**
     * Update a todo.
     * @param todo the todo to be updated.
     */
    @Update
    fun updateTodo(todo: Todo)

    /**
     * Sort todos by low priority.
     * @return sorted todos.
     */
    @Query("SELECT * FROM todo_table ORDER BY CASE priority WHEN 'LOW' THEN 1 WHEN 'MEDIUM' THEN 2 WHEN 'HIGH' THEN 3 ELSE 4 END ASC")
    fun sortByLowPriority(): LiveData<List<Todo>>

    /**
     * Sort todos by high priority.
     * @return sorted todos.
     */
    @Query("SELECT * FROM todo_table ORDER BY CASE priority WHEN 'HIGH' THEN 1 WHEN 'MEDIUM' THEN 2 WHEN 'LOW' THEN 3 ELSE 4 END ASC")
    fun sortByHighPriority(): LiveData<List<Todo>>

    /**
     * Sort todos by newest.
     * @return sorted todos.
     */
    @Query("SELECT * FROM todo_table ORDER BY id DESC")
    fun sortByNewest():LiveData<List<Todo>>

    /**
     * Sort todos by oldest.
     * @return sorted todos.
     */
    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun sortByOldest(): LiveData<List<Todo>>

    /**
     * Sort todos by title from A to Z.
     * @return sorted todos.
     */
    @Query("SELECT * FROM todo_table ORDER BY title ASC")
    fun sortByTitleAZ(): LiveData<List<Todo>>

    /**
     * Sort todos by title from Z to A.
     * @return sorted todos.
     */
    @Query("SELECT * FROM todo_table ORDER BY title DESC")
    fun sortByTitleZA(): LiveData<List<Todo>>
}