package com.chicohan.samplelistapp.data.repository

import com.chicohan.samplelistapp.data.entity.SampleListItem
import kotlinx.coroutines.flow.Flow

interface ToDoListRepository {

    suspend fun addToDoList(item: SampleListItem)

    suspend fun getToDoItems(userId: Int): Flow<List<SampleListItem>>

    suspend fun updateTodoItems(itemId: Int, task: SampleListItem)

    suspend fun deleteTodoItem(itemId: Int)
}