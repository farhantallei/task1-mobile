package com.example.myapplication.validator

class CityValidator : Validator<Int>{
    override fun execute(input: Int): String? {
        return if (input == 0) "Please select a city" else null
    }
}