package com.example.reuniteapp.ui.notifications

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reuniteapp.R
import com.example.reuniteapp.ui.home.ItemsViewModel
import com.example.reuniteapp.models.Items

class NotificationsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotificationsAdapter

    private val itemsViewModel: ItemsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewNotifications)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("USER_ID", -1)

        itemsViewModel.items.observe(viewLifecycleOwner, Observer { items ->
            val userItems = items.filter { it.foundBy == userId } // Filter by user ID
            val notifications = userItems.sortedByDescending { it.date + it.time } // Sort by date and time
            adapter = NotificationsAdapter(notifications)
            recyclerView.adapter = adapter
        })

        return view
    }

    override fun onResume() {
        super.onResume()
        itemsViewModel.loadItems() // Reload items when fragment becomes visible
    }
}