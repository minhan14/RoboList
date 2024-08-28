package com.chicohan.samplelistapp.domain

import android.content.Context
import com.chicohan.samplelistapp.data.entity.User
import com.chicohan.samplelistapp.data.repository.AuthenticationRepository
import com.chicohan.samplelistapp.domain.model.Resource
import com.chicohan.samplelistapp.helper.UserPreferences
import com.chicohan.samplelistapp.helper.toast
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CancellationException
import javax.inject.Inject

data class LoginUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val scope: CoroutineDispatcher = Dispatchers.IO,
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(
        name: String,
        password: String
    ): Resource<User> = withContext(scope) {
        try {
            if (name.isEmpty() || password.isEmpty()) {
                return@withContext Resource.Error(
                    "Fields cannot be empty",
                    data = null
                )
            }
            val result = authenticationRepository.authenticateUser(name, password)
            result.fold(
                onSuccess = {
                    UserPreferences.saveUser(context, it)
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
            e.printStackTrace()
            return@withContext Resource.Error(e.message.toString(), data = null)
        } catch (e: CancellationException) {
            //in case job cancelled
            throw CancellationException(e.message)
        }
    }
}