package com.chicohan.samplelistapp.helper

import android.content.Context
import com.chicohan.samplelistapp.data.entity.User

object UserPreferences {
    private const val PREFS_NAME = "user_prefs"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_NAME = "user_name"
    fun saveUser(context: Context, user: User) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putInt(KEY_USER_ID, user.id)
            putString(KEY_USER_NAME, user.name)
            apply()
        }
    }

    fun getUser(context: Context): User? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt(KEY_USER_ID, -1)
        val userName = sharedPreferences.getString(KEY_USER_NAME, null)
        return if (userId != -1 && userName != null) {
            /**
             * we will not store password
             */
            User(id = userId, name = userName, encryptedPassword = "")
        } else {
            null
        }
    }

    fun clearUser(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }

}