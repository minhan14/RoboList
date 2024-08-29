package com.chicohan.samplelistapp.domain
import javax.inject.Inject

data class UseCases @Inject constructor(
    val registerUseCase: RegisterUseCase,
    val loginUseCase: LoginUseCase,
    val toDoOperationUseCase:ToDoOperationUseCase
)