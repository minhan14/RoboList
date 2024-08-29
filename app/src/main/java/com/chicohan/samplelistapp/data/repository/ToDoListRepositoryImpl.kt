package com.chicohan.samplelistapp.data.repository

import com.chicohan.samplelistapp.data.database.AppDatabase
import com.chicohan.samplelistapp.data.entity.SampleListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ToDoListRepositoryImpl @Inject constructor(
    private val db: AppDatabase
) : ToDoListRepository {

    override suspend fun addToDoList(item: SampleListItem) {
        withContext(Dispatchers.IO) {
            db.toToDao().insertToDoItem(item)
        }
    }

    override suspend fun getToDoItems(userId: Int): Flow<List<SampleListItem>> {
        return db.toToDao().getToDoItemsForUser(userId = userId)
    }

    override suspend fun updateTodoItems(itemId: Int, task: SampleListItem) {
        withContext(Dispatchers.IO) {
            db.toToDao().updateToDoItemById(
                itemId = itemId,
                name = task.name,
                imageUri = task.imageUri,
                description = task.description
            )
        }

    }

}