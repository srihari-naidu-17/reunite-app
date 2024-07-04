package com.example.reuniteapp.ui

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.reuniteapp.R
import com.example.reuniteapp.models.Items
import com.example.reuniteapp.ui.home.ItemsViewModel
import com.example.reuniteapp.ui.profile.UserProfileViewModel

class AddItemActivity : AppCompatActivity() {

    private lateinit var editTextItemTitle: EditText
    private lateinit var editTextItemDescription: EditText
    private lateinit var editTextItemLocation: EditText
    private lateinit var editTextItemCategory: EditText
    private lateinit var buttonSaveItem: Button

    private val userProfileViewModel: UserProfileViewModel by viewModels()
    private lateinit var itemsViewModel: ItemsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        editTextItemTitle = findViewById(R.id.editTextItemTitle)
        editTextItemDescription = findViewById(R.id.editTextItemDescription)
        editTextItemLocation = findViewById(R.id.editTextItemLocation)
        editTextItemCategory = findViewById(R.id.editTextItemCategory)
        buttonSaveItem = findViewById(R.id.buttonSaveItem)

        itemsViewModel = ViewModelProvider(this).get(ItemsViewModel::class.java)

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

                    itemsViewModel.addItem(newItem)
                    Toast.makeText(this@AddItemActivity, "Item saved successfully", Toast.LENGTH_SHORT).show()
                    finish() // Close the activity after saving the item

                }, {
                    Toast.makeText(this, "Failed to retrieve user information", Toast.LENGTH_SHORT).show()
                })
            } else {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
