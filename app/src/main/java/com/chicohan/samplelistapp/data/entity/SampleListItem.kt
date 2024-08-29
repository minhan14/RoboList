package com.chicohan.samplelistapp.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "todo_items",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)

data class SampleListItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int = 0,
    val name: String,
    val imageUri: String?,
    val description: String
) : Parcelable
