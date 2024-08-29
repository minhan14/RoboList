package com.chicohan.samplelistapp.domain

import com.chicohan.samplelistapp.data.repository.ToDoListRepository
import javax.inject.Inject

data class ToDoOperationUseCase @Inject constructor(
    private val toDoListRepository: ToDoListRepository
){

}
