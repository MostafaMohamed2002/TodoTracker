package com.mostafadevo.todo.presentation.signup

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth

class SignUpViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var mFirebaseAuth: FirebaseAuth

    init {
        mFirebaseAuth = FirebaseAuth.getInstance()
    }

    fun signUpWithEmailAndPassword(email: String, password: String): Boolean {
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("SignUpViewModel", "signUpWithEmail:success")
                } else {
                    Log.d("SignUpViewModel", "signUpWithEmail:failure")
                }
            }
            .addOnFailureListener() { e ->
                Log.d("SignUpViewModel", "signUpWithEmail:failure", e)
            }
        return true
    }
}