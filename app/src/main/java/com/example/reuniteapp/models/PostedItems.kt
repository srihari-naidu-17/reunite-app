package com.example.reuniteapp.models

data class PostedItems(
    val imageUrl: String,
    val itemName: String,
    val location: String,
    val date: String,
    val time: String,
    val itemDescription: String,
    val itemType: String,
)