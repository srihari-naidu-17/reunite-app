package com.example.reuniteapp.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.graphics.drawable.toBitmap
import com.example.reuniteapp.R
import com.example.reuniteapp.models.Items
import com.example.reuniteapp.models.UserProfile
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import kotlin.math.absoluteValue

object SampleData {

    var phonenumbercount = 1;

    fun insertSampleData(context: Context) {
        GlobalScope.launch {
            val database = AppDatabase.getDatabase(context)
            val userProfileDao = database.userProfileDao()
            val itemsDao = database.itemsDao()

            // Check if the database is empty
            if (userProfileDao.getCount() == 0 && itemsDao.getCount() == 0) {
                // Create and insert sample user profiles
                val userProfiles = listOf(
                    createUserProfile(context, "naga", R.drawable.profile_naga),
                    createUserProfile(context, "suka", R.drawable.profile_suka),
                    createUserProfile(context, "hari", R.drawable.profile_hari),
                    createUserProfile(context, "david", R.drawable.profile_david)
                )
                userProfiles.forEach { userProfileDao.insert(it) }

                // Create and insert sample items
                val items = listOf(
                    // Lost Item items
                    createItem(context, 1, "Black iPhone", "Black iPhone lost at FCI", "10/06/2024", "14:30", R.drawable.item_phone, "Faculty of Computing & Informatics (FCI)", "Lost Item"),
                    createItem(context, 2, "Brown Wallet", "Brown leather wallet lost at FOE", "11/06/2024", "09:15", R.drawable.item_wallet, "Faculty of Engineering (FOE)", "Lost Item"),

                    // Found Item items
                    createItem(context, 3, "Science Fiction Book", "Science fiction book found in SHDL", "16/06/2024", "15:20", R.drawable.item_book, "Siti Hasmah Digital Library", "Found Item"),
                    createItem(context, 4, "Smartwatch", "Smartwatch found in LP", "17/06/2024", "08:45", R.drawable.item_watch, "Learning Point (LP)", "Found Item"),
                    createItem(context, 1, "Ray-Ban Sunglasses", "Ray-Ban sunglasses found in HTC", "18/06/2024", "10:00", R.drawable.item_sunglasses, "Haji Tapah Cafe (HTC)", "Found Item")
                )
                items.forEach { itemsDao.insert(it) }
            }
        }
    }

    private fun createUserProfile(context: Context, username: String, imageResId: Int): UserProfile {
        val contactNumber = "123456789${phonenumbercount}"
        phonenumbercount += 1;
        val email = "$username@gmail.com"
        val profileImageName = saveProfileImageToInternalStorage(context, username, imageResId)
        return UserProfile(
            name = username.capitalize(),
            contactNumber = contactNumber,
            username = username,
            password = username,
            profileImageUri = profileImageName,
            email = email
        )
    }

    private fun createItem(
        context: Context,
        foundById: Int,
        itemTitle: String,
        itemDescription: String,
        date: String,
        time: String,
        drawableResId: Int,
        location: String,
        category: String
    ): Items {
        var email = ""
        if (foundById==1)
            email = "naga@gmail.com"
        if (foundById==2)
            email = "suka@gmail.com"
        if (foundById==3)
            email = "hari@gmail.com"
        if (foundById==4)
            email = "david@gmail.com"
        val contactNumber = "123456789${foundById}"
        val itemImageName = saveImageToInternalStorage(context, "item_${itemTitle}_${System.currentTimeMillis()}", drawableResId)
        return Items(
            foundBy = foundById,
            reunited = false,
            itemImage = itemImageName,
            itemTitle = itemTitle,
            location = location,
            date = date,
            time = time,
            itemDescription = itemDescription,
            itemCategory = category,
            userEmail = email,
            contactNumber = contactNumber
        )
    }

    private fun saveImageToInternalStorage(context: Context, fileNamePrefix: String, drawableResId: Int): String {
        val directory = File(context.filesDir, "images")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val fileName = "$fileNamePrefix.jpg"
        val file = File(directory, fileName)
        if (!file.exists()) {
            val bitmap = BitmapFactory.decodeResource(context.resources, drawableResId)
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.close()
        }
        return fileName
    }

    private fun saveProfileImageToInternalStorage(context: Context, fileName: String, drawableResId: Int): String {
        val profileName = "$fileName.jpg"
        context.openFileOutput(profileName, Context.MODE_PRIVATE).use { fos ->
            val bitmap = BitmapFactory.decodeResource(context.resources, drawableResId)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        }

        return profileName
    }


}
