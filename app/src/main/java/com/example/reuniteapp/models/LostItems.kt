package com.example.reuniteapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lost_items")
data class LostItems(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val dateLost: String,
    val locationLost: String,
    val contactInfo: String = "",
    val imageUri: String = ""
)