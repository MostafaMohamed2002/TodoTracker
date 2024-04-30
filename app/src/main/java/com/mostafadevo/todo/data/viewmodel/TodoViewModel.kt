package com.mostafadevo.todo.data.viewmodel

import android.app.Application
import android.widget.ResourceCursorTreeAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mostafadevo.todo.R
import com.mostafadevo.todo.data.TodoDataBase
import com.mostafadevo.todo.data.model.Todo
import com.mostafadevo.todo.data.repo.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    class TodoViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TodoViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private val _priorityColor = MutableLiveData<Int>()
    val priorityColor: LiveData<Int>
        get() = _priorityColor
    private val repository: TodoRepository
    private val todoDAO = TodoDataBase.getDatabase(application).todoDao()
    private val getAllTodo: LiveData<List<Todo>>

    init {
        repository = TodoRepository(todoDAO)
        getAllTodo = repository.getAllTodo
    }

    fun insertTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertTodo(todo)
        }
    }

    fun getAllTodos(): LiveData<List<Todo>> {
        return getAllTodo
    }

    fun deleteAllTodos() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTodos()
        }
    }

    fun setSpinnerPriorityColor(position: Int) {
        _priorityColor.value = when (position) {
            0 -> {
                R.color.priority_high
            }

            1 -> {
                R.color.priority_medium
            }

            2 -> {
                R.color.priority_low
            }

            else -> {
                android.R.color.transparent
            }
        }

    }

}