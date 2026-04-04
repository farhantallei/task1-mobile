package com.example.myapplication.validator

class PasswordValidator : Validator<String> {
    override fun execute(input: String): String? {
        if (input.isEmpty()) return "Password cannot be empty"
        if (input.length < 6) return "Password should be at least 6 characters"
        return null
    }
}
