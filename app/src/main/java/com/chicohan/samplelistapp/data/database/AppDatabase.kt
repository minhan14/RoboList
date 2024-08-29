package com.chicohan.samplelistapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chicohan.samplelistapp.data.dao.ToDoDao
import com.chicohan.samplelistapp.data.dao.UserDao
import com.chicohan.samplelistapp.data.entity.SampleListItem
import com.chicohan.samplelistapp.data.entity.User

@Database(entities = [User::class,SampleListItem::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun toToDao(): ToDoDao
}