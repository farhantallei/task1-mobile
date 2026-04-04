package com.example.myapplication.validator

interface Validator<T> {
    fun execute(input: T): String?
}
