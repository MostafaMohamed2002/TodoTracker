package com.mostafadevo.todo.presentation.signup

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout
import com.mostafadevo.todo.R
import com.mostafadevo.todo.Utils
import com.mostafadevo.todo.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var _binding: ActivitySignUpBinding
    private val viewModel: SignUpViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        validateInputFieldsAndSignUp()

    }

    private fun validateInputFieldsAndSignUp() {
        _binding.signupPageButton.setOnClickListener {
            val email = _binding.signUpEmailTextinput.editText?.text.toString().trim()
            val password = _binding.signUpPasswordTextinput.editText?.text.toString().trim()
            val confirmPassword =
                _binding.signUpConfirmPasswordTextinput.editText?.text.toString().trim()

            when {
                email.isEmpty() -> {
                    showError(_binding.signUpEmailTextinput, "Email field cannot be empty")
                }

                password.isEmpty() -> {
                    showError(_binding.signUpPasswordTextinput, "Password field cannot be empty")
                }

                confirmPassword.isEmpty() -> {
                    showError(
                        _binding.signUpConfirmPasswordTextinput,
                        "Confirm password field cannot be empty"
                    )
                }

                !Utils.validateConfirmPassword(password, confirmPassword) -> {
                    showError(_binding.signUpConfirmPasswordTextinput, "Password does not match")
                    showError(_binding.signUpPasswordTextinput, "Password does not match")
                }

                !Utils.validateEmail(email) -> {
                    showError(_binding.signUpEmailTextinput, "Invalid email format")
                }

                !Utils.validatePassword(password) -> {
                    showError(
                        _binding.signUpPasswordTextinput,
                        "Password must be at least 6 characters"
                    )
                }

                else -> {
                    if (viewModel.signUpWithEmailAndPassword(email, password)) {
                        finish()
                    }
                }
            }
        }
    }

    private fun showError(textInput: TextInputLayout, errorMessage: String) {
        textInput.error = errorMessage
        textInput.postDelayed({ textInput.error = null }, 2000)
    }

}