package com.mostafadevo.todo.data.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mostafadevo.todo.data.repo.ProfileRepository
import kotlinx.coroutines.launch


class ProfileViewModel() : ViewModel() {
    private val repository: ProfileRepository = ProfileRepository()
    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String> get() = _imageUrl

    init {
        getProfileImageUrl()
    }

    fun uploadImage(imageUri: Uri) {
        viewModelScope.launch {
            val url = repository.uploadImageToFirebase(imageUri)
            _imageUrl.value = url
        }
    }

    fun getEmail(): CharSequence {
        //get email from firebase
        return repository.getEmail().toString()
    }

    fun getName(): CharSequence {
        //get name from firebase
        return repository.getName().toString()
    }

    fun getProfileImageUrl() {
        //get profile image url from firebase
        viewModelScope.launch {
            val imageUrl = repository.getProfileImageUrl().toString()
            _imageUrl.value = imageUrl
        }
    }
}