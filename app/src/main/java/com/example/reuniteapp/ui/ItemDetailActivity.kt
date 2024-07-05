package com.example.reuniteapp.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.reuniteapp.R
import com.example.reuniteapp.data.AppDatabase
import com.example.reuniteapp.data.ItemsDao
import com.example.reuniteapp.models.Items
import kotlinx.coroutines.launch
import android.graphics.BitmapFactory
import android.view.View
import com.example.reuniteapp.ui.profile.UserProfileViewModel
import java.io.File
import java.io.FileInputStream

class ItemDetailActivity : AppCompatActivity() {

    private lateinit var itemTitle: TextView
    private lateinit var itemDescription: TextView
    private lateinit var itemLocation: TextView
    private lateinit var itemDate: TextView
    private lateinit var itemContact: TextView
    private lateinit var itemImage: ImageView
    private lateinit var buttonCallOwner: Button
    private lateinit var buttonMarkReunited: Button
    private lateinit var item: Items
    private lateinit var itemsDao: ItemsDao

    companion object {
        private const val REQUEST_READ_STORAGE_PERMISSION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        itemTitle = findViewById(R.id.itemTitle)
        itemDescription = findViewById(R.id.itemDescription)
        itemLocation = findViewById(R.id.itemLocation)
        itemDate = findViewById(R.id.itemDate)
        itemContact = findViewById(R.id.itemContact)
        itemImage = findViewById(R.id.itemImage)
        buttonCallOwner = findViewById(R.id.buttonCallOwner)
        buttonMarkReunited = findViewById(R.id.buttonMarkReunited)

        val database = AppDatabase.getDatabase(this)
        itemsDao = database.itemsDao()

        val itemId = intent.getIntExtra("ITEM_ID", -1)
        if (itemId != -1) {
            loadItemDetails(itemId)
        } else {
            Toast.makeText(this, "Invalid item ID", Toast.LENGTH_SHORT).show()
            finish()
        }

        buttonCallOwner.setOnClickListener {
            callOwner()
        }

        buttonMarkReunited.setOnClickListener {
            markItemReunited()
        }

        checkAndRequestPermissions()
    }

    private fun checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_STORAGE_PERMISSION)
            }
        }
    }

    private fun loadItemDetails(itemId: Int) {
        lifecycleScope.launch {
            item = itemsDao.getItemById(itemId) ?: run {
                Toast.makeText(this@ItemDetailActivity, "Item not found", Toast.LENGTH_SHORT).show()
                finish()
                return@launch
            }

            itemTitle.text = item.itemTitle
            itemDescription.text = item.itemDescription
            itemLocation.text = item.location
            itemDate.text = item.date
            itemContact.text = item.contactNumber

            // Load the image from internal storage
            val filePath = applicationContext.filesDir.path + "/images/" + item.itemTitle + ".jpg"
            val file = File(filePath)
            if (file.exists()) {
                val bitmap = BitmapFactory.decodeStream(FileInputStream(file))
                itemImage.setImageBitmap(bitmap)
            }

            val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getInt("USER_ID", -1)

            val userProfileViewModel: UserProfileViewModel by viewModels()
            userProfileViewModel.getUserProfileById(userId, { userProfile ->
                if (userProfile.username == item.foundBy) {
                    buttonMarkReunited.visibility = View.VISIBLE
                } else {
                    buttonMarkReunited.visibility = View.GONE
                }
            }, {
                Toast.makeText(this@ItemDetailActivity, "Failed to retrieve user information", Toast.LENGTH_SHORT).show()
            })
        }
    }

    private fun callOwner() {
        val phoneNumber = item.contactNumber.trim()
        if (phoneNumber.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "No app to handle the call", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show()
        }
    }

    private fun markItemReunited() {
        val updatedItem = item.copy(reunited = true)
        lifecycleScope.launch {
            itemsDao.update(updatedItem)
            Toast.makeText(this@ItemDetailActivity, "Item marked as reunited", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}