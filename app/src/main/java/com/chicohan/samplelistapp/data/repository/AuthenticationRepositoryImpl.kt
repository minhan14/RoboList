package com.chicohan.samplelistapp.data.repository

import com.chicohan.samplelistapp.data.dao.UserDao
import com.chicohan.samplelistapp.data.entity.User
import com.chicohan.samplelistapp.helper.EncryptionHelper
import javax.inject.Inject


class AuthenticationRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
) : AuthenticationRepository {

    private val secretKey = EncryptionHelper.getKey()

    override suspend fun registerUser(name: String, password: String): Result<User> {
        try {
            val existingUser = userDao.getUserByName(name)
            if (existingUser != null) return Result.failure(Exception("Name already taken"))
            val key = secretKey ?: return Result.failure(Exception("no key found"))
            val encryptedPassword = EncryptionHelper.encrypt(password, key)
            val user = User(name = name, encryptedPassword = encryptedPassword)
            userDao.insertUser(user)
            return Result.success(user)
        } catch (e: Throwable) {
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun authenticateUser(name: String, password: String): Result<User> {
        val user = userDao.getUserByName(name) ?: return Result.failure(Exception("User not found"))
        val key = secretKey ?: return Result.failure(Exception("no key found"))
        val decryptedPassword = EncryptionHelper.decrypt(user.encryptedPassword, key)
        return if (password == decryptedPassword) {
            Result.success(user)
        } else {
            Result.failure(Exception("Invalid credentials"))
        }
    }
}