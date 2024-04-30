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
}