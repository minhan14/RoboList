package com.chicohan.samplelistapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chicohan.samplelistapp.data.entity.SampleListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToDoItem(toDoItem: SampleListItem): Long

    @Query("SELECT * FROM todo_items WHERE userId = :userId")
    fun getToDoItemsForUser(userId: Int): Flow<List<SampleListItem>>

    @Query("UPDATE todo_items SET name = :name, imageUri = :imageUri, description = :description WHERE id = :itemId")
    suspend fun updateToDoItemById(itemId: Int, name: String, imageUri: String?, description: String): Int

    @Query("DELETE FROM todo_items WHERE id = :itemId")
    suspend fun deleteToDoItemById(itemId: Int): Int
}