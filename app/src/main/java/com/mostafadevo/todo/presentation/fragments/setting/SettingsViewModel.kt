package com.mostafadevo.todo.presentation.fragments.setting

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mostafadevo.todo.FirebaseSyncWorker
import com.mostafadevo.todo.data.database.TodoDataBase
import com.mostafadevo.todo.data.model.Todo
import com.mostafadevo.todo.data.repo.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val todoDAO = TodoDataBase.getDatabase(application).todoDao()
    private val repository: TodoRepository = TodoRepository(todoDAO)
    private var todosFromFirebase: List<Todo> = emptyList()
    private val _userName: MutableLiveData<String> = MutableLiveData()
    val userName: LiveData<String> = _userName

    private val _userEmail: MutableLiveData<String> = MutableLiveData()
    val userEmail: LiveData<String> = _userEmail

    private val _userImageUrl: MutableLiveData<String> = MutableLiveData()
    val userImageUrl: LiveData<String> = _userImageUrl

    init {
        getUserName()
        getUserEmail()
        getUserImageUrl()
    }

    fun getUserEmail() {
        viewModelScope.launch(Dispatchers.IO) {
            val userMail = repository.getUserEmail()
            _userEmail.postValue(userMail)
        }
    }

    fun getUserName() {
        viewModelScope.launch(Dispatchers.IO) {
            val userName: String = repository.getUserName()
            _userName.postValue(userName)
        }
    }

    fun getUserImageUrl() {
        viewModelScope.launch(Dispatchers.IO) {
            val imageUrl: String = repository.getUserImageUrl()
            _userImageUrl.postValue(imageUrl)
        }
    }

    fun pushTodosToFirebase() {
        //how to handle success and failure
        viewModelScope.launch(Dispatchers.IO) {
            repository.pushTodosToFirebase(
                repository.getAllTodos(),
                repository.getAllDeletedTodos()
            )
        }
    }

    fun getTodosFromFirebase() {
        viewModelScope.launch(Dispatchers.IO) {
            todosFromFirebase = repository.getTodosFromFirebase()
            todosFromFirebase.forEach {
                todoDAO.insertTodo(it)
            }
        }
    }

    fun startFirebaseSyncService() {
        FirebaseSyncWorker.schedule(getApplication())
        Log.d("SettingsViewModel", "startFirebaseSyncService")
    }

    fun stopFirebaseSyncService() {
        val context = getApplication<Application>().applicationContext
        val workManager = androidx.work.WorkManager.getInstance(context)
        workManager.cancelUniqueWork(FirebaseSyncWorker.WORK_NAME)
        Log.d("SettingsViewModel", "stopFirebaseSyncService")
    }

    fun uploadImageToFirebase(picUri: Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.uploadImageToFirebase(picUri)
        }
    }

    fun updateUserName(newUserName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUserName(newUserName)
        }
    }
}