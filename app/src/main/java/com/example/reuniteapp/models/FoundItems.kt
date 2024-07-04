package com.example.reuniteapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "found_items")
data class FoundItems(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val dateFound: String,
    val locationFound: String,
    val contactInfo: String = "",
    val imageUri: String = ""
)