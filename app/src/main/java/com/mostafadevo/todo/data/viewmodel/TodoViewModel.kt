package com.mostafadevo.todo.data.viewmodel

import android.app.Application
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mostafadevo.todo.R
import com.mostafadevo.todo.data.TodoDataBase
import com.mostafadevo.todo.data.model.Todo
import com.mostafadevo.todo.data.repo.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {


    private val repository: TodoRepository
    private val todoDAO = TodoDataBase.getDatabase(application).todoDao()

    private val _sortType = MutableStateFlow<String>("newest")
    val sortType: StateFlow<String> = _sortType.asStateFlow()

    val prioritySelectionListener: AdapterView.OnItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
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
    }
    val sortedData = _sortType
        .flatMapLatest { sortType ->
            repository.getTodoSortedBy(sortType) // Assuming you adapt the repository to return Flow
        }
        .asLiveData(viewModelScope.coroutineContext)



    //database operations
    fun insertTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertTodo(todo)
        }
    }

    fun deleteAllTodos() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTodos()
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTodo(todo)
        }
    }
    fun setSortType(newSortType: String) {
        _sortType.value = newSortType
    }


}