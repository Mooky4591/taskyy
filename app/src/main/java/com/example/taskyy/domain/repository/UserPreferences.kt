package com.example.taskyy.domain.repository

import com.example.taskyy.domain.objects.AuthenticatedUser

interface UserPreferences {
    fun saveAuthenticatedUser(user: AuthenticatedUser, key: String)
    fun clearPreferences()
    fun addUserFullName(fullName: String, key: String)
    fun getUserFullName(key: String): String
    fun addUserEmail(email: String, key: String)
    fun getUserEmail(key: String): String
    fun addUserId(userId: String, key: String)
    fun getUserId(key: String): String
    fun addUserToken(token: String, key: String)
    fun getUserToken(key: String): String
}