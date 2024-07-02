package com.example.reuniteapp.ui.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.reuniteapp.R
import com.example.reuniteapp.data.AppDatabase
import com.example.reuniteapp.data.UserProfileDao
import com.example.reuniteapp.databinding.FragmentProfileBinding
import com.example.reuniteapp.models.UserProfile
import kotlinx.coroutines.launch
import java.io.OutputStream

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

        userProfileDao = AppDatabase.getDatabase(requireContext()).userProfileDao()

        viewLifecycleOwner.lifecycleScope.launch {
            // Assume we have the current user's ID stored in SharedPreferences
            val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
            val userId = sharedPref.getInt("USER_ID", -1)
            if (userId != -1) {
                val userProfile = userProfileDao.getUserProfileById(userId)
                userProfile?.let { updateUI(it) }
            }
        }

//        binding.editProfileButton.setOnClickListener {
//            // Navigate to edit profile screen
//            // You'll need to implement this navigation
//        }
    }

    private fun updateUI(userProfile: UserProfile) {
        binding.nameTextView.text = userProfile.name
        binding.emailTextView.text = userProfile.email
        binding.contactNumberTextView.text = userProfile.contactNumber

        // Load profile image
        val profileImage = loadProfileImage(requireContext(), userProfile)
        if (profileImage != null) {
            binding.profileImage.setImageBitmap(profileImage)
        } else {
            binding.profileImage.setImageResource(R.drawable.default_profile_image)
        }
    }

    private fun loadProfileImage(context: Context, userProfile: UserProfile): Bitmap? {
        return try {
            val imageFile = context.getFileStreamPath(userProfile.profileImageUri)
            BitmapFactory.decodeFile(imageFile.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun saveProfileImage(context: Context, imageUri: Uri, userProfile: UserProfile) {
        val fileName = "profile_${userProfile.id}.jpg"
        val outputStream: OutputStream
        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            outputStream.close()

            // Update the user profile with the new image URI
            userProfile.profileImageUri = fileName
            viewLifecycleOwner.lifecycleScope.launch {
                userProfileDao.updateUserProfile(userProfile)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}