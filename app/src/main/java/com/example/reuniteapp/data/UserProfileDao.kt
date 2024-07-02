package com.example.reuniteapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.reuniteapp.models.UserProfile

@Dao
interface UserProfileDao {
    @Insert
    suspend fun insert(userProfile: UserProfile)

    @Query("SELECT * FROM user_profiles WHERE email = :email")
    suspend fun getUserProfileByEmail(email: String): UserProfile?
}