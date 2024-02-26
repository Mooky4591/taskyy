package com.example.taskyy.domain.repository

interface AuthRepository {

    fun isEmailValid(email: String): Boolean
    fun isNameValid(name: String): Boolean
    //need to replace these parameters with a user object
    fun registerUser(name: String, email: String, password: String)
}