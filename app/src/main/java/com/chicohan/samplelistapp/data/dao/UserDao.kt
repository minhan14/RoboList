package com.chicohan.samplelistapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.chicohan.samplelistapp.data.entity.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE name = :name")
    suspend fun getUserByName(name: String): User?


}