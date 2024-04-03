package com.example.taskyy.data.repositories

import android.content.SharedPreferences
import com.example.taskyy.domain.objects.AuthenticatedUser
import com.example.taskyy.domain.repository.UserPreferences
import com.google.gson.Gson

class UserPreferencesImpl(
    private val sharedPref: SharedPreferences
) : UserPreferences {
    override fun saveAuthenticatedUser(user: AuthenticatedUser, key: String) {
        val gson = Gson()
        val json = gson.toJson(user)
        sharedPref.edit().putString(key, json).apply()
    }

    override fun clearPreferences() {
        sharedPref.edit().clear().apply()
    }

    override fun addUserFullName(fullName: String, key: String) {
        sharedPref.edit().putString(key, fullName).apply()
    }

    override fun getUserFullName(key: String): String {
        val name = sharedPref.getString(key, "")
        return name!!
    }

    override fun addUserEmail(email: String, key: String) {
        sharedPref.edit().putString(key, email).apply()

    }

    override fun getUserEmail(key: String): String {
        val email = sharedPref.getString(key, "")
        return email!!
    }

    override fun addUserId(userId: String, key: String) {
        sharedPref.edit().putString(key, userId).apply()
    }

    override fun getUserId(key: String): String {
        val userId = sharedPref.getString(key, "")
        return userId!!
    }

    override fun addUserToken(token: String, key: String) {
        sharedPref.edit().putString(key, token).apply()
    }

    override fun getUserToken(key: String): String {
        val token = sharedPref.getString(key, "")
        return token!!
    }
}