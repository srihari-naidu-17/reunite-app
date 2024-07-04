package com.example.reuniteapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.reuniteapp.models.LostItems

@Dao
interface LostItemsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLostItem(lostItem: LostItems)

    @Update
    suspend fun updateLostItem(lostItem: LostItems)

    @Delete
    suspend fun deleteLostItem(lostItem: LostItems)

    @Query("SELECT * FROM lost_items WHERE id = :id")
    suspend fun getLostItemById(id: Int): LostItems?

    @Query("SELECT * FROM lost_items ORDER BY dateLost DESC")
    fun getAllLostItems(): LiveData<List<LostItems>>
}