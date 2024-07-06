package com.example.reuniteapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.reuniteapp.models.Items

@Dao
interface ItemsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Items)

    @Update
    suspend fun update(item: Items)

    @Query("SELECT * FROM items WHERE foundBy = :username")
    suspend fun getItemsByUser(username: String): List<Items>

    @Query("SELECT * FROM items WHERE itemId = :itemId")
    suspend fun getItemById(itemId: Int): Items?

    @Query("SELECT * FROM items")
    suspend fun getAllItems(): List<Items>

    @Query("SELECT * FROM items WHERE reunited = :reunited")
    suspend fun getItemsByReunitedStatus(reunited: Boolean): List<Items>

    @Query("SELECT COUNT(*) FROM items")
    suspend fun getCount(): Int
}