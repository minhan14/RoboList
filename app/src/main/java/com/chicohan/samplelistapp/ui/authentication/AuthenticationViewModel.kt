package com.chicohan.samplelistapp.ui.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chicohan.samplelistapp.data.entity.User
import com.chicohan.samplelistapp.data.repository.AuthenticationRepository
import com.chicohan.samplelistapp.domain.Event
import com.chicohan.samplelistapp.domain.UseCases
import com.chicohan.samplelistapp.domain.model.Resource
import com.chicohan.samplelistapp.domain.model.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val useCases: UseCases
) :
    ViewModel() {

    var registerUserState = MutableStateFlow(AuthenticationUiState())
        private set

    var loginUserState = MutableStateFlow(AuthenticationUiState())
        private set

    fun registerUser(name: String, password: String, confirmPassword: String) =
        viewModelScope.launch {
            this@AuthenticationViewModel.registerUserState.update { it.copy(loading = Event(true)) }
            when (val state = useCases.registerUseCase(
                name = name,
                password = password,
                confirmPassword = confirmPassword
            )) {
                is Resource.Error -> registerUserState.update {
                    it.copy(
                        loading = Event(false),
                        isSuccess = Event(null),
                        errorMessage = Event(state.message)
                    )
                }

                is Resource.Loading -> Unit

                is Resource.Success -> registerUserState.update {
                    it.copy(
                        loading = Event(false),
                        isSuccess = Event(state.data),
                        errorMessage = Event(null)
                    )
                }
            }
        }


    fun authenticateUser(name: String, password: String) = viewModelScope.launch {
        this@AuthenticationViewModel.loginUserState.update { it.copy(loading = Event(true)) }
        when (val state = useCases.loginUseCase(name = name, password = password)) {
            is Resource.Error -> loginUserState.update {
                it.copy(
                    loading = Event(false),
                    isSuccess = Event(null),
                    errorMessage = Event(state.message)
                )
            }

            is Resource.Loading -> Unit

            is Resource.Success -> loginUserState.update {
                it.copy(
                    loading = Event(false),
                    isSuccess = Event(state.data),
                    errorMessage = Event(null)
                )
            }
        }
    }
}

data class AuthenticationUiState(
    var loading: Event<Boolean> = Event(false),
    val isSuccess: Event<User?> = Event(null),
    val errorMessage: Event<String?> = Event(null),
)