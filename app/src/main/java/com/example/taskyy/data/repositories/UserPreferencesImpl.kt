package com.example.taskyy.data.repositories

import android.content.SharedPreferences
import com.example.taskyy.domain.objects.AuthenticatedUser
import com.example.taskyy.domain.repository.UserPreferences
import com.google.gson.Gson

class UserPreferencesImpl(
    private val sharedPref: SharedPreferences,
) : UserPreferences {
    override fun saveAuthenticatedUser(user: AuthenticatedUser, key: String) {
        val gson = Gson()
        val json = gson.toJson(user)
        sharedPref.edit().putString(key, json).apply()
    }

    override fun clearPreferences() {
        sharedPref.edit().clear().apply()
    }

    override fun addUserFullName(fullName: String) {
        sharedPref.edit().putString("name", fullName).apply()
    }

    override fun getUserFullName(): String {
        val name = sharedPref.getString("name", "")
        return name!!
    }

    override fun addUserEmail(email: String) {
        sharedPref.edit().putString("email", email).apply()

    }

    override fun getUserEmail(): String {
        val email = sharedPref.getString("email", "")
        return email!!
    }

    override fun addUserId(userId: String) {
        sharedPref.edit().putString("id", userId).apply()
    }

    override fun getUserId(): String {
        val userId = sharedPref.getString("id", "")
        return userId!!
    }

    override fun addUserToken(token: String) {
        sharedPref.edit().putString("token", token).apply()
    }

    override fun getUserToken(): String {
        val token = sharedPref.getString("token", "")
        return token!!
    }
}