package com.example.reuniteapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String = "",
    var contactNumber: String = "",
    var username: String = "",
    var password: String = "",
    var profileImageUri: String = "",
    val email: String,
    val profilePic: String
)