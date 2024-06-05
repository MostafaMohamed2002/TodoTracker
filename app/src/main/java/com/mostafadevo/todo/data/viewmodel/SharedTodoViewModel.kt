package com.mostafadevo.todo.data.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.mostafadevo.todo.NotificationReceiver
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
    val _sortType = MutableLiveData<String>("title A to Z")
    val sortedData = MutableLiveData<List<Todo>>()
    val searchedTodos = MutableLiveData<List<Todo>>()
    var currentUser: String? = null

    init {
        repository = TodoRepository(todoDAO)
        _sortType.observeForever { newSortType ->
            repository.getLocalTodoSortedBy(newSortType).observeForever { sortedTodos ->
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
        scheduleNotification(getApplication(), todo)
    }

    fun deleteAllTodos() {
        viewModelScope.launch(Dispatchers.IO) {
            sortedData.value?.let { cancelAllNotifications(getApplication(), it) }
            repository.deleteAllTodos()
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateLocalTodo(todo)
        }
        scheduleNotification(getApplication(), todo)
    }

    fun setSortType(newSortType: String) {
        _sortType.value = newSortType
    }

    fun deleteTodoItem(itemToDelete: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteLocalTodo(itemToDelete)
        }
        cancelNotification(getApplication(), itemToDelete)
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
    fun scheduleNotification(context: Context, todo: Todo) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("title", todo.title)
            putExtra("message", todo.description)
            putExtra("id", todo.id.hashCode())
        }

        val pendingIntent = PendingIntent.getBroadcast(context, todo.id.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // setExact is used to set the alarm to before the exact time by 1 minute
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, todo.dateAndTime.time, pendingIntent)
        Log.d(
            "SharedTodoViewModel",
            "scheduleNotification: ${todo.title} ${todo.description} ${todo.dateAndTime}"
        )
    }
    //cancel notification if todo is deleted
    fun cancelNotification(context: Context, todo: Todo) {
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, todo.id.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
    //cancel all notifications
    fun cancelAllNotifications(context: Context,todos: List<Todo>) {
        todos.forEach {
            val intent = Intent(context, NotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, it.id.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
        }
    }

}