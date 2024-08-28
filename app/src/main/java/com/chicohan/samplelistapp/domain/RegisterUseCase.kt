package com.chicohan.samplelistapp.domain

import android.content.Context
import com.chicohan.samplelistapp.data.entity.User
import com.chicohan.samplelistapp.data.repository.AuthenticationRepository
import com.chicohan.samplelistapp.domain.model.Resource
import com.chicohan.samplelistapp.helper.UserPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class RegisterUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val scope: CoroutineDispatcher = Dispatchers.IO,
) {
    suspend operator fun invoke(
        name: String,
        password: String,
        confirmPassword: String
    ): Resource<User> = withContext(scope) {
        try {
            if (password.isEmpty() || name.isEmpty() || confirmPassword.isEmpty()) {
                return@withContext Resource.Error("Fields can't be empty", data = null)
            }
            if (password != confirmPassword) {
                return@withContext Resource.Error("password do not match", data = null)
            }
            if (password.length < 6) {
                return@withContext Resource.Error(
                    "Password should be at least 6 characters",
                    data = null
                )
            }
            val result = authenticationRepository.registerUser(name, password)
            result.fold(
                onSuccess = {
                    return@withContext Resource.Success(it)
                },
                onFailure = {
                    return@withContext Resource.Error(
                        it.localizedMessage ?: "error",
                        data = null
                    )
                }
            )
        } catch (e: Throwable) {
            return@withContext Resource.Error(e.message.toString(), data = null)
        } catch (e: CancellationException) {
            //in case job cancelled
            throw CancellationException(e.message)
        }
    }
}
