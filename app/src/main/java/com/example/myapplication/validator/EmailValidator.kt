package com.example.myapplication.validator

class EmailValidator : Validator<String> {
    override fun execute(input: String): String? {
        if (input.isEmpty()) return "Email cannot be empty"
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(input)
                .matches()
        ) return "Email is not valid"
        return null
    }
}
