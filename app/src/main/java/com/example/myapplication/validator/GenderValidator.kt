package com.example.myapplication.validator

class GenderValidator : Validator<Int> {
    override fun execute(input: Int): String? {
        return if (input == -1) "Choose gender" else null
    }
}
