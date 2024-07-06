package com.example.reuniteapp.ui.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.reuniteapp.R
import com.example.reuniteapp.data.AppDatabase
import com.example.reuniteapp.data.UserProfileDao
import com.example.reuniteapp.databinding.FragmentEditProfileBinding
import com.example.reuniteapp.models.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class EditProfileFragment : Fragment() {

    private val userProfileViewModel: UserProfileViewModel by viewModels()

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var userProfileDao: UserProfileDao
    private var currentUserId: Int = -1
    private var profilePicBitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hide the back arrow button
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        // Initialize userProfileDao
        val database = AppDatabase.getDatabase(requireContext())
        userProfileDao = database.userProfileDao()

        // Load user profile data
        loadUserProfile()

        // Set click listeners
        binding.saveButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                saveUserProfile()
            }
        }

        binding.cancelButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.editProfileImageButton.setOnClickListener {
            // Launch image picker to select profile picture
            pickProfileImage.launch("image/*")
        }
    }

    private fun loadUserProfile() {
        viewLifecycleOwner.lifecycleScope.launch {
            val userId = getUserIdFromSharedPreferences()
            if (userId != -1) {
                userProfileViewModel.getUserProfileById(userId,
                    onSuccess = { userProfile ->
                        updateUI(userProfile)
                        Log.d(TAG, "User profile loaded successfully")
                    },
                    onError = { e ->
                        Log.e(TAG, "Error loading user profile", e)
                        Toast.makeText(requireContext(), "Failed to load user profile", Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                Log.e(TAG, "User ID not found in SharedPreferences")
                Toast.makeText(requireContext(), "Please log in to edit your profile", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateUI(userProfile: UserProfile) {
        binding.nameEditText.setText(userProfile.name)
        binding.emailEditText.setText(userProfile.email)
        binding.contactEditText.setText(userProfile.contactNumber)

        // Load and display the profile image
        val profileImage = loadProfileImage(userProfile.profileImageUri)
        if (profileImage != null) {
            binding.profileImageView.setImageBitmap(profileImage)
        } else {
            binding.profileImageView.setImageResource(R.drawable.default_profile_image)
        }
    }

    private fun saveUserProfile() {
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val contactNumber = binding.contactEditText.text.toString()
        val userId = getUserIdFromSharedPreferences()

        viewLifecycleOwner.lifecycleScope.launch {
            userProfileViewModel.getUserProfileById(userId,
                onSuccess = { userProfile ->
                    userProfile.apply {
                        this.name = name
                        this.email = email
                        this.contactNumber = contactNumber
                        val newImageUri = saveProfileImageToInternalStorage()
                        if (newImageUri.isNotEmpty()) {
                            this.profileImageUri = newImageUri
                        }
                    }

                    userProfileViewModel.updateUserProfile(userProfile,
                        onSuccess = {
                            Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
                            requireActivity().supportFragmentManager.popBackStack()
                        },
                        onError = { e ->
                            Toast.makeText(requireContext(), "Failed to update profile: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                onError = { e ->
                    Log.e(TAG, "Error retrieving user profile", e)
                    Toast.makeText(requireContext(), "Failed to load user profile", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun saveProfileImageToInternalStorage(): String {
        profilePicBitmap?.let { bitmap ->
            val fileName = "profile_pic_${System.currentTimeMillis()}.jpg"
            requireContext().openFileOutput(fileName, Context.MODE_PRIVATE).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
            }
            return fileName
        }
        return ""
    }

    private fun getUserIdFromSharedPreferences(): Int {
        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("USER_ID", -1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadProfileImage(imageUri: String): Bitmap? {
        return try {
            if (imageUri.isNotEmpty()) {
                val imageFile = File(requireContext().filesDir, imageUri)
                BitmapFactory.decodeFile(imageFile.absolutePath)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading profile image", e)
            null
        }
    }

    private fun decodeImageFromFile(imageUri: String): Bitmap? {
        return try {
            val imageStream = requireContext().contentResolver.openInputStream(android.net.Uri.parse(imageUri))
            BitmapFactory.decodeStream(imageStream)
        } catch (e: Exception) {
            Log.e(TAG, "Error decoding image from file", e)
            null
        }
    }

    companion object {
        private const val TAG = "EditProfileFragment"
    }

    // ActivityResultLauncher for image picker
    private val pickProfileImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        // Handle the selected image URI here
        uri?.let {
            // Update profilePicUri with the selected image URI
            val imageUriString = it.toString()
            profilePicBitmap = decodeImageFromFile(imageUriString)
            binding.profileImageView.setImageBitmap(profilePicBitmap)
        }
    }
}
