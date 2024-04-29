package com.mostafadevo.todo.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.mostafadevo.todo.data.TodoDataBase
import com.mostafadevo.todo.data.model.Todo
import com.mostafadevo.todo.data.repo.TodoRepository
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TodoRepository
    private val todoDAO = TodoDataBase.getDatabase(application).todoDao()
    private val getAllTodo: LiveData<List<Todo>>

    init {
        repository = TodoRepository(todoDAO)
        getAllTodo = repository.getAllTodo
    }
    fun insertTodo(todo: Todo){
        viewModelScope.launch {
            repository.insertTodo(todo)
        }
    }
}