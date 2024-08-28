package com.chicohan.samplelistapp.data.entity

import androidx.room.Entity

@Entity
data class SampleListItem(
    val id:Int,
    val imageUri:String,
    val description:String
)
