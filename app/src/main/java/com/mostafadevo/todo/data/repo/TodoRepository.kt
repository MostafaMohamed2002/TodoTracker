package com.mostafadevo.todo.data.repo

import androidx.lifecycle.LiveData
import com.mostafadevo.todo.data.TodoDAO
import com.mostafadevo.todo.data.model.Todo

class TodoRepository(private val todoDAO: TodoDAO) {

    val getAllTodo: LiveData<List<Todo>> = todoDAO.getAllTodos()

    suspend fun insertTodo(todo: Todo) {
        todoDAO.insertTodo(todo)
    }
    suspend fun deleteAllTodos() {
        todoDAO.deleteAllTodos()
    }
    suspend fun updateTodo(todo: Todo){
        todoDAO.updateTodo(todo)
    }

     fun getTodoSortedBy(sortType: String): LiveData<List<Todo>> {
        return when (sortType) {
            "newest" -> todoDAO.sortByNewest()
            "oldest" -> todoDAO.sortByOldest()
            "high priority" -> todoDAO.sortByHighPriority()
            "low priority" -> todoDAO.sortByLowPriority()
            "title A to Z" -> todoDAO.sortByTitleAZ()
            "title Z to A" -> todoDAO.sortByTitleZA()
            else -> todoDAO.sortByOldest()
        }
    }

    suspend fun deleteTodoItem(itemToDelete: Todo) {
        todoDAO.deleteTodoItem(itemToDelete)
    }
}