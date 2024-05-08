package com.mostafadevo.todo.view.login

import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.mostafadevo.todo.R

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private const val RC_SIGN_IN = 9001
        private const val TAG = "LoginViewModel"
    }

    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> get() = _loginStatus

    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var gso: GoogleSignInOptions

    val sharedPreferences: SharedPreferences = application.getSharedPreferences("LOGIN_STATUS", 0)

    private val _signInIntent = MutableLiveData<Intent>()
    val signInIntent: LiveData<Intent> get() = _signInIntent

    init {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(application, R.string.default_web_client_id))
            .requestEmail()
            .build()
        mFirebaseAuth = FirebaseAuth.getInstance()
        googleSignInClient = GoogleSignIn.getClient(application, gso)

    }

    fun signInWithGoogle() {
        _signInIntent.value = googleSignInClient.signInIntent
    }

    fun signInWithEmailAndPassword(email: String?, password: String?) {
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            _loginStatus.value = false
            return
        }

        mFirebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _loginStatus.value = task.isSuccessful
                setUserLoggedIn(task.isSuccessful)
                Log.d("LoginViewModel", "signInWithEmail:onComplete: ${task.isSuccessful}")
            }
            .addOnFailureListener() { e ->
                _loginStatus.value = false
                setUserLoggedIn(false)
                Log.w("LoginViewModel", "signInWithEmail:failure", e)
            }
    }

    fun setUserLoggedIn(loggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("IS_LOGGED_IN", loggedIn)
        editor.apply()
    }

}