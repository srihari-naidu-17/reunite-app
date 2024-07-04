package com.example.reuniteapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.reuniteapp.models.FoundItems

@Dao
interface FoundItemsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoundItem(foundItem: FoundItems)

    @Update
    suspend fun updateFoundItem(foundItem: FoundItems)

    @Delete
    suspend fun deleteFoundItem(foundItem: FoundItems)

    @Query("SELECT * FROM found_items WHERE id = :id")
    suspend fun getFoundItemById(id: Int): FoundItems?

    @Query("SELECT * FROM found_items ORDER BY dateFound DESC")
    fun getAllFoundItems(): LiveData<List<FoundItems>>
}