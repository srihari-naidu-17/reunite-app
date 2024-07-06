package com.example.reuniteapp.ui.report

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.appcompat.widget.Toolbar
import com.example.reuniteapp.R
import com.example.reuniteapp.data.AppDatabase
import com.example.reuniteapp.data.ItemsDao
import com.example.reuniteapp.models.Items
import com.example.reuniteapp.ui.home.ItemsViewModel
import com.example.reuniteapp.ui.profile.UserProfileViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Calendar

class ReportActivity : AppCompatActivity() {

    private lateinit var editTextItemTitle: EditText
    private lateinit var editTextItemDescription: EditText
    private lateinit var editTextItemLocation: EditText
    private lateinit var editTextItemCategory: Spinner
    private lateinit var editTextDate: EditText
    private lateinit var editTextTime: EditText
    private lateinit var editTextContactInfo: EditText
    private lateinit var imageViewItem: ImageView
    private lateinit var buttonUploadImage: Button
    private lateinit var buttonSaveItem: Button
    private lateinit var itemsDao: ItemsDao
    private var imageUri: Uri? = null

    private val userProfileViewModel: UserProfileViewModel by viewModels()
    private lateinit var itemsViewModel: ItemsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        // Set up the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Enable the back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        editTextItemTitle = findViewById(R.id.nameOfItem)
        editTextItemDescription = findViewById(R.id.descOfItem)
        editTextItemLocation = findViewById(R.id.locationOfItem)
        editTextItemCategory = findViewById(R.id.itemStatusSpinner)
        editTextDate = findViewById(R.id.dateOfItem)
        editTextTime = findViewById(R.id.timeOfItem)

        editTextDate.setOnClickListener {
            showDatePicker()
        }

        editTextTime.setOnClickListener {
            showTimePicker()
        }
        editTextContactInfo = findViewById(R.id.contactInfo)
        imageViewItem = findViewById(R.id.imageViewItem)
        buttonUploadImage = findViewById(R.id.buttonUploadImage)
        buttonSaveItem = findViewById(R.id.doneButton)

        itemsViewModel = ViewModelProvider(this).get(ItemsViewModel::class.java)
        val database = AppDatabase.getDatabase(this)
        itemsDao = database.itemsDao()

        buttonUploadImage.setOnClickListener {
            checkPermissionAndOpenFileChooser()
        }

        buttonSaveItem.setOnClickListener {
            saveItem()
        }

        // Set up spinner for item status
        ArrayAdapter.createFromResource(
            this,
            R.array.item_status_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            editTextItemCategory.adapter = adapter
        }
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(this, { _, year, month, day ->
            val date = "${day}/${month + 1}/${year}"
            editTextDate.setText(date)
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val timePickerDialog = TimePickerDialog(this, { _, hour, minute ->
            val time = "${hour}:${minute}"
            editTextTime.setText(time)
        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true)
        timePickerDialog.show()
    }

    private fun checkPermissionAndOpenFileChooser() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_MEDIA_IMAGES), REQUEST_READ_MEDIA_IMAGES)
            } else {
                openFileChooser()
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_EXTERNAL_STORAGE)
            } else {
                openFileChooser()
            }
        }
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFileChooser()
            } else {
                Toast.makeText(this, "Permission denied to read external storage", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == REQUEST_READ_MEDIA_IMAGES) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFileChooser()
            } else {
                Toast.makeText(this, "Permission denied to read media images", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                imageViewItem.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun saveItem() {
        val itemTitle = editTextItemTitle.text.toString()
        val itemDescription = editTextItemDescription.text.toString()
        val itemLocation = editTextItemLocation.text.toString()
        val itemCategory = editTextItemCategory.selectedItem.toString()
        val itemDate = editTextDate.text.toString()
        val itemTime = editTextTime.text.toString()
        val itemContactInfo = editTextContactInfo.text.toString()
        val itemImage = imageUri?.toString() ?: ""

        if (itemTitle.isNotEmpty() && itemDescription.isNotEmpty() && itemLocation.isNotEmpty() && itemImage.isNotEmpty()) {
            val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getInt("USER_ID", -1)

            userProfileViewModel.getUserProfileById(userId, { userProfile ->
                val userEmail = userProfile.email

                val newItem = Items(
                    foundBy = userId, // Store the user ID here
                    reunited = false,
                    itemImage = itemImage,
                    itemTitle = itemTitle,
                    location = itemLocation,
                    date = itemDate,
                    time = itemTime,
                    itemDescription = itemDescription,
                    itemCategory = itemCategory,
                    contactNumber = itemContactInfo,
                    userEmail = userEmail
                )

                lifecycleScope.launch {
                    try {
                        // Save image to file
                        val directory = File(this@ReportActivity.filesDir, "images")
                        if (!directory.exists()) {
                            directory.mkdirs()
                        }
                        val itemImageName = "item_${System.currentTimeMillis()}.jpg"
                        val file = File(directory, itemImageName)
                        val outputStream = FileOutputStream(file)
                        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                        outputStream.close()
                        newItem.itemImage = itemImageName

                        itemsDao.insert(newItem)
                        itemsViewModel.loadItems() // Reload items to reflect the new addition
                        Toast.makeText(this@ReportActivity, "Item saved successfully", Toast.LENGTH_SHORT).show()
                        finish() // Close the activity after saving the item
                    } catch (e: Exception) {
                        Toast.makeText(this@ReportActivity, "Failed to save item", Toast.LENGTH_SHORT).show()
                    }
                }
            }, {
                Toast.makeText(this, "Failed to retrieve user information", Toast.LENGTH_SHORT).show()
            })
        } else {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val REQUEST_READ_EXTERNAL_STORAGE = 2
        private const val REQUEST_READ_MEDIA_IMAGES = 3
    }
}