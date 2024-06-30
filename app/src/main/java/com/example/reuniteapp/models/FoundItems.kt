package com.example.reuniteapp.models

data class FoundItems(
    val imageUrl: String,
    val itemName: String,
    val location: String,
    val date: String,
    val time: String,
    val itemDescription: String,
    val itemCategory: String,
    val username: String,
    val email: String,
    val contactNumber: String
)