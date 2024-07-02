package com.example.reuniteapp.ui.profile

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.reuniteapp.R
import com.example.reuniteapp.data.AppDatabase
import com.example.reuniteapp.data.UserProfileDao
import com.example.reuniteapp.databinding.FragmentProfileBinding
import com.example.reuniteapp.models.UserProfile
import com.example.reuniteapp.ui.LoginActivity
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var userProfileDao: UserProfileDao

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = getUserIdFromSharedPreferences()
        Log.d("com.example.reuniteapp.ui.profile.ProfileFragment", "User ID from SharedPreferences: $userId")

        // Initialize userProfileDao
        val database = AppDatabase.getDatabase(requireContext())
        userProfileDao = database.userProfileDao()
        Log.d("com.example.reuniteapp.ui.profile.ProfileFragment", "userProfileDao initialized")

        loadUserProfile()

        binding.editProfileButton.setOnClickListener {
            // TODO: Implement navigation to edit profile screen
            Toast.makeText(requireContext(), "Edit Profile clicked", Toast.LENGTH_SHORT).show()
        }

        binding.logoutButton.setOnClickListener {
            logoutUser()
        }
    }

    private fun logoutUser() {
        // Clear user session or preferences
        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        // Navigate back to login screen
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun loadUserProfile() {
        viewLifecycleOwner.lifecycleScope.launch {
            val userId = getUserIdFromSharedPreferences()
            Log.d("com.example.reuniteapp.ui.profile.ProfileFragment", "User ID from SharedPreferences: $userId")

            if (userId != -1) {
                try {
                    val userProfile = userProfileDao.getUserProfileById(userId)
                    if (userProfile != null) {
                        updateUI(userProfile)
                        Log.d("com.example.reuniteapp.ui.profile.ProfileFragment", "User profile loaded successfully")
                    } else {
                        Log.e("com.example.reuniteapp.ui.profile.ProfileFragment", "User profile not found for ID: $userId")
                        Toast.makeText(requireContext(), "User profile not found", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("com.example.reuniteapp.ui.profile.ProfileFragment", "Error retrieving user profile", e)
                    Toast.makeText(requireContext(), "Failed to load user profile", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.e("com.example.reuniteapp.ui.profile.ProfileFragment", "User ID not found in SharedPreferences")
                Toast.makeText(requireContext(), "Please log in to view your profile", Toast.LENGTH_LONG).show()
                // TODO: Navigate to login screen if not logged in
            }
        }
    }

    private fun updateUI(userProfile: UserProfile) {
        binding.nameTextView.text = userProfile.name
        binding.emailTextView.text = userProfile.email
        binding.contactNumberTextView.text = userProfile.contactNumber

        // Load profile image if available
        val profileImage = loadProfileImage(requireContext(), userProfile)
        if (profileImage != null) {
            binding.profileImage.setImageBitmap(profileImage)
        } else {
            binding.profileImage.setImageResource(R.drawable.default_profile_image)
        }
    }

    private fun loadProfileImage(context: Context, userProfile: UserProfile): Bitmap? {
        return try {
            if (userProfile.profileImageUri.isNotEmpty()) {
                val imageFile = context.getFileStreamPath(userProfile.profileImageUri)
                BitmapFactory.decodeFile(imageFile.absolutePath)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("com.example.reuniteapp.ui.profile.ProfileFragment", "Error loading profile image", e)
            null
        }
    }
    private fun getUserIdFromSharedPreferences(): Int {
        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("USER_ID", -1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}