package com.example.taskyy.domain.repository

import com.example.taskyy.domain.objects.AuthenticatedUser

interface UserPreferences {
    fun saveAuthenticatedUser(user: AuthenticatedUser, key: String)
    fun clearPreferences()
    fun addUserFullName(fullName: String)
    fun getUserFullName(): String
    fun addUserEmail(email: String)
    fun getUserEmail(): String
    fun addUserId(userId: String)
    fun getUserId(): String
    fun addUserToken(token: String)
    fun getUserToken(): String
}