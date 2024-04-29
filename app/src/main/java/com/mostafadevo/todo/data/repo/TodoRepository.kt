package com.mostafadevo.todo.data.repo

import androidx.lifecycle.LiveData
import com.mostafadevo.todo.data.model.Todo
import com.mostafadevo.todo.data.TodoDAO

class TodoRepository(private val todoDAO: TodoDAO) {

    val getAllTodo : LiveData<List<Todo>> = todoDAO.getAllTodos()

   suspend fun insertTodo(todo: Todo){
       todoDAO.insertTodo(todo)
   }
}