package com.example.myapplication.validator

class HobbyValidator : Validator<List<Boolean>> {
    override fun execute(input: List<Boolean>): String? {
        return if (input.none { it }) "Choose at least 1 hobby" else null
    }
}
