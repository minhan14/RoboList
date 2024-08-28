package com.chicohan.samplelistapp.data.repository

import com.chicohan.samplelistapp.data.entity.User

interface AuthenticationRepository {

    suspend fun registerUser(name: String, password: String): Result<User>
    suspend fun authenticateUser(name: String, password: String): Result<User>

}