package com.example.reuniteapp.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reuniteapp.R
import com.example.reuniteapp.data.AppDatabase
import com.example.reuniteapp.data.ItemsDao
import com.example.reuniteapp.models.Items
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope

class ItemsListActivity : AppCompatActivity() {

    private lateinit var recyclerViewItems: RecyclerView
    private lateinit var itemsDao: ItemsDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items_list)

        recyclerViewItems = findViewById(R.id.recyclerViewItems)
        recyclerViewItems.layoutManager = LinearLayoutManager(this)

        val database = AppDatabase.getDatabase(this)
        itemsDao = database.itemsDao()

        lifecycleScope.launch {
            try {
                val items = itemsDao.getItemsByFoundStatus(false)
                recyclerViewItems.adapter = ItemsAdapter(items) { item ->
                    itemFoundOrReturned(item)
                }
            } catch (e: Exception) {
                Toast.makeText(this@ItemsListActivity, "Failed to load items", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun itemFoundOrReturned(item: Items) {
        val updatedItem = item.copy(found = true)
        lifecycleScope.launch {
            try {
                itemsDao.update(updatedItem)
                Toast.makeText(this@ItemsListActivity, "Item status updated", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@ItemsListActivity, "Failed to update item status", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
