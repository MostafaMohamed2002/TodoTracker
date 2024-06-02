package com.mostafadevo.todo.data.viewmodel

import android.app.Application
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.mostafadevo.todo.R
import com.mostafadevo.todo.data.TodoDataBase
import com.mostafadevo.todo.data.model.Todo
import com.mostafadevo.todo.data.repo.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedTodoViewModel(application: Application) : AndroidViewModel(application) {


    private val repository: TodoRepository
    private val todoDAO = TodoDataBase.getDatabase(application).todoDao()
    private lateinit var mFirebaseAuth: FirebaseAuth

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
    val _sortType = MutableLiveData<String>("newest")
    val sortedData = MutableLiveData<List<Todo>>()
    val searchedTodos = MutableLiveData<List<Todo>>()
    var currentUser: String? = null

    init {
        repository = TodoRepository(todoDAO)
        _sortType.observeForever { newSortType ->
            repository.getTodoSortedBy(newSortType).observeForever { sortedTodos ->
                sortedData.postValue(sortedTodos)
            }
        }
        mFirebaseAuth = FirebaseAuth.getInstance()
        currentUser = mFirebaseAuth.currentUser?.displayName
    }


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

    fun deleteTodoItem(itemToDelete: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTodoItem(itemToDelete)
        }
    }

    fun search(query: String) {
        viewModelScope.launch() {
            repository.search(query).observeForever { result ->
                searchedTodos.postValue(result)
            }
        }
    }

    fun logout(googleSignInClient: GoogleSignInClient) {
        mFirebaseAuth.signOut()
        googleSignInClient.signOut()
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllLocalTodos()
        }
    }


}