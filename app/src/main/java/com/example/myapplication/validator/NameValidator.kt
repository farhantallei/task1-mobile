package com.example.myapplication.validator

class NameValidator : Validator<String> {
    override fun execute(input: String): String? {
        return if (input.isEmpty()) "Name cannot be empty" else null
    }
}
