package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.myapplication.validator.ConfirmPasswordValidator
import com.example.myapplication.validator.EmailValidator
import com.example.myapplication.validator.GenderValidator
import com.example.myapplication.validator.HobbyValidator
import com.example.myapplication.validator.NameValidator
import com.example.myapplication.validator.PasswordValidator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import androidx.core.content.edit

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)
        val isRegistered = sharedPref.getBoolean("isRegistered", false)

        if (isRegistered) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        val fullName = findViewById<TextInputEditText>(R.id.full_name)
        val fullNameField = findViewById<TextInputLayout>(R.id.full_name_field)
        val email = findViewById<TextInputEditText>(R.id.email)
        val emailField = findViewById<TextInputLayout>(R.id.email_field)
        val gender = findViewById<RadioGroup>(R.id.gender)
        val genderError = findViewById<TextView>(R.id.gender_error)
        val hobbyFoodie = findViewById<CheckBox>(R.id.hobby_foodie)
        val hobbyTraveling = findViewById<CheckBox>(R.id.hobby_traveling)
        val hobbyError = findViewById<TextView>(R.id.hobby_error)
        val password = findViewById<TextInputEditText>(R.id.password)
        val passwordField = findViewById<TextInputLayout>(R.id.password_field)
        val confirmPassword = findViewById<TextInputEditText>(R.id.confirm_password)
        val confirmPasswordField = findViewById<TextInputLayout>(R.id.confirm_password_field)
        val registerBtn = findViewById<Button>(R.id.register_btn)

        val nameValidator = NameValidator()
        val emailValidator = EmailValidator()
        val passwordValidator = PasswordValidator()
        val genderValidator = GenderValidator()
        val hobbyValidator = HobbyValidator()

        fun showError(textView: TextView, error: String?) {
            if (error != null) {
                textView.text = error
                textView.visibility = View.VISIBLE
            } else {
                textView.visibility = View.GONE
            }
        }

        fun validateHobby(): String? {
            val error = hobbyValidator.execute(listOf(hobbyFoodie.isChecked, hobbyTraveling.isChecked))
            showError(hobbyError, error)
            return error
        }

        fun validateGender(): String? {
            val error = genderValidator.execute(gender.checkedRadioButtonId)
            showError(genderError, error)
            return error
        }

        fun validateForm(): Boolean {
            val fullNameError = nameValidator.execute(fullName.text.toString())
            val emailError = emailValidator.execute(email.text.toString())
            val passwordValue = password.text.toString()
            val passwordError = passwordValidator.execute(passwordValue)
            val confirmPasswordError =
                ConfirmPasswordValidator(passwordValue).execute(confirmPassword.text.toString())
            val genderErrorMsg = validateGender()
            val hobbyErrorMsg = validateHobby()

            fullNameField.error = fullNameError
            emailField.error = emailError
            passwordField.error = passwordError
            confirmPasswordField.error = confirmPasswordError

            return listOf(
                fullNameError,
                emailError,
                passwordError,
                confirmPasswordError,
                genderErrorMsg,
                hobbyErrorMsg
            ).all { it == null }
        }

        fullName.addTextChangedListener {
            fullNameField.error = nameValidator.execute(it.toString())
        }

        email.addTextChangedListener {
            emailField.error = emailValidator.execute(it.toString())
        }

        password.addTextChangedListener {
            val value = it.toString()
            passwordField.error = passwordValidator.execute(value)

            confirmPassword.text?.let {
                confirmPasswordField.error = ConfirmPasswordValidator(value).execute(it.toString())
            }
        }

        confirmPassword.addTextChangedListener {
            confirmPasswordField.error =
                ConfirmPasswordValidator(password.text.toString()).execute(it.toString())
        }

        gender.setOnCheckedChangeListener { _, _ -> validateGender() }

        hobbyFoodie.setOnCheckedChangeListener { _, _ -> validateHobby() }
        hobbyTraveling.setOnCheckedChangeListener { _, _ -> validateHobby() }

        registerBtn.setOnClickListener {
            if (validateForm()) {
                AlertDialog.Builder(this)
                    .setTitle("Confirmation")
                    .setMessage("Are you sure all the data is correct?")
                    .setPositiveButton("Yes") { _, _ ->
                        sharedPref.edit {
                            putBoolean("isRegistered", true)
                            putString("name", fullName.text.toString())
                            putString("email", email.text.toString())
                        }

                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
