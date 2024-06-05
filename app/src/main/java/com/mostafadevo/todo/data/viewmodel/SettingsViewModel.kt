package com.mostafadevo.todo.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mostafadevo.todo.data.TodoDataBase
import com.mostafadevo.todo.data.repo.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val todoDAO = TodoDataBase.getDatabase(application).todoDao()
    private val repository: TodoRepository = TodoRepository(todoDAO)

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
    fun getUserEmail(){
        viewModelScope.launch(Dispatchers.IO) {
            val userMail= repository.getUserEmail()
            _userEmail.postValue(userMail)
        }
    }
    fun getUserName(){
        viewModelScope.launch(Dispatchers.IO) {
            val userName :String = repository.getUserName()
            _userName.postValue(userName)
        }
    }
    fun getUserImageUrl(){
        viewModelScope.launch(Dispatchers.IO){
            val imageUrl :String = repository.getUserImageUrl()
            _userImageUrl.postValue(imageUrl)
        }
    }

    fun pushTodosToFirebase(){}
    fun getTodosFromFirebase(){}

    fun setupFirebaseSync(){}
}