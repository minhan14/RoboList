package com.chicohan.samplelistapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chicohan.samplelistapp.data.dao.UserDao
import com.chicohan.samplelistapp.data.entity.User

@Database(entities = [User::class], version = 2)
abstract class AppDatabase:RoomDatabase() {
    abstract fun userDao(): UserDao
}