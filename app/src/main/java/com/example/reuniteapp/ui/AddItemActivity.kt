package com.example.reuniteapp.ui

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.reuniteapp.R
import com.example.reuniteapp.data.AppDatabase
import com.example.reuniteapp.data.ItemsDao
import com.example.reuniteapp.models.Items
import com.example.reuniteapp.ui.profile.UserProfileViewModel
import kotlinx.coroutines.launch

class AddItemActivity : AppCompatActivity() {

    private lateinit var editTextItemTitle: EditText
    private lateinit var editTextItemDescription: EditText
    private lateinit var editTextItemLocation: EditText
    private lateinit var editTextItemCategory: EditText
    private lateinit var buttonSaveItem: Button
    private lateinit var itemsDao: ItemsDao

    private val userProfileViewModel: UserProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        editTextItemTitle = findViewById(R.id.editTextItemTitle)
        editTextItemDescription = findViewById(R.id.editTextItemDescription)
        editTextItemLocation = findViewById(R.id.editTextItemLocation)
        editTextItemCategory = findViewById(R.id.editTextItemCategory)
        buttonSaveItem = findViewById(R.id.buttonSaveItem)

        val database = AppDatabase.getDatabase(this)
        itemsDao = database.itemsDao()

        buttonSaveItem.setOnClickListener {
            val itemTitle = editTextItemTitle.text.toString()
            val itemDescription = editTextItemDescription.text.toString()
            val itemLocation = editTextItemLocation.text.toString()
            val itemCategory = editTextItemCategory.text.toString()
            val itemImage = "imagePath" // Replace with actual image path

            if (itemTitle.isNotEmpty() && itemDescription.isNotEmpty() && itemLocation.isNotEmpty()) {
                val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                val userId = sharedPreferences.getInt("USER_ID", -1)

                userProfileViewModel.getUserProfileById(userId, { userProfile ->
                    val foundBy = userProfile.username

                    val newItem = Items(
                        foundBy = foundBy,
                        found = false,
                        itemImage = itemImage,
                        itemTitle = itemTitle,
                        location = itemLocation,
                        itemDescription = itemDescription,
                        itemCategory = itemCategory
                    )

                    lifecycleScope.launch {
                        try {
                            itemsDao.insert(newItem)
                            Toast.makeText(this@AddItemActivity, "Item saved successfully", Toast.LENGTH_SHORT).show()
                            finish() // Close the activity after saving the item
                        } catch (e: Exception) {
                            Toast.makeText(this@AddItemActivity, "Failed to save item", Toast.LENGTH_SHORT).show()
                        }
                    }
                }, {
                    Toast.makeText(this, "Failed to retrieve user information", Toast.LENGTH_SHORT).show()
                })
            } else {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
