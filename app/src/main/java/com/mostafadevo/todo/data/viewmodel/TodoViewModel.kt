package com.mostafadevo.todo.data.viewmodel

import android.app.Application
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
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



    private val repository: TodoRepository
    private val todoDAO = TodoDataBase.getDatabase(application).todoDao()
    private val getAllTodo: LiveData<List<Todo>>
    val listner: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            when (position) {
                0 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(
                        ContextCompat.getColor(
                            application,
                            R.color.priority_high
                        )
                    )
                }

                1 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(
                        ContextCompat.getColor(
                            application,
                            R.color.priority_medium
                        )
                    )
                }

                2 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(
                        ContextCompat.getColor(
                            application,
                            R.color.priority_low
                        )
                    )
                }
            }

        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }

    init {
        repository = TodoRepository(todoDAO)
        getAllTodo = repository.getAllTodo
    }

    //database operations
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


}