package com.example.myapplication.validator

class ConfirmPasswordValidator(private val password: String) : Validator<String> {
    override fun execute(input: String): String? {
        if (input.isEmpty()) return "Password cannot be empty"
        if (input != password) return "Password is not match"
        return null
    }
}
