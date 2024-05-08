package com.mostafadevo.todo.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.mostafadevo.todo.MainActivity
import com.mostafadevo.todo.R
import com.mostafadevo.todo.databinding.ActivityLoginBinding
import com.mostafadevo.todo.view.signup.SignUpActivity

class LoginActivity : AppCompatActivity() {
    companion object {
        private const val RC_SIGN_IN = 9001
        private const val TAG = "LoginActivity"
    }

    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var _binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onStart() {
        super.onStart()
        mFirebaseAuth = FirebaseAuth.getInstance()
        if (mFirebaseAuth.currentUser != null) {
            Log.d(TAG, "current user is ${mFirebaseAuth.currentUser}")
            if (mFirebaseAuth.currentUser?.isEmailVerified != null) {
                Log.d(TAG, "User is already logged in ${mFirebaseAuth.currentUser?.email}")
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(_binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        _binding.signinWithGoogle.setOnClickListener {
            viewModel.signInWithGoogle()
        }

        viewModel.signInIntent.observe(this, Observer { intent ->
            startActivityForResult(intent, RC_SIGN_IN)
        })


        viewModel.loginStatus.observe(this) { isLoggedIn ->
            if (isLoggedIn) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(
                    this@LoginActivity,
                    "Invalid Email or Password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        _binding.loginButton.setOnClickListener {
            val email = _binding.emailTextinput.editText?.text.toString().trim()
            val password = _binding.passwordTextinput.editText?.text.toString().trim()
            // validate email and password and sign in in view model
            viewModel.signInWithEmailAndPassword(email, password)
        }


        _binding.gotoSignupPageButton.setOnClickListener() {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

    }

    //on activity result for google sign in
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account.idToken)
                } catch (e: ApiException) {
                    Log.w(TAG, "Google sign in failed", e)
                }
            } else {
                Log.w(TAG, "onActivityResult Error :${exception.toString()}")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mFirebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mFirebaseAuth.currentUser
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}