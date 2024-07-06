package com.example.reuniteapp.ui.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reuniteapp.R
import com.example.reuniteapp.models.Items
import com.example.reuniteapp.ui.ItemsAdapter
import com.example.reuniteapp.ui.ItemDetailActivity
import com.example.reuniteapp.ui.home.ItemsViewModel

class DashboardFragment : Fragment() {

    private lateinit var recyclerViewLostItems: RecyclerView
    private lateinit var recyclerViewFoundItems: RecyclerView
    private lateinit var recyclerViewReunitedItems: RecyclerView

    private val itemsViewModel: ItemsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        recyclerViewLostItems = view.findViewById(R.id.recyclerViewLostItems)
        recyclerViewFoundItems = view.findViewById(R.id.recyclerViewFoundItems)
        recyclerViewReunitedItems = view.findViewById(R.id.recyclerViewReunitedItems)

        recyclerViewLostItems.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewFoundItems.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewReunitedItems.layoutManager = LinearLayoutManager(requireContext())

        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("USER_ID", -1)

        itemsViewModel.items.observe(viewLifecycleOwner, Observer { items ->
            val userItems = items.filter { it.foundBy == userId } // Filter by user ID
            val lostItems = userItems.filter { !it.reunited && it.itemCategory == "Lost Item" }
            val foundItems = userItems.filter { !it.reunited && it.itemCategory == "Found Item" }
            val reunitedItems = userItems.filter { it.reunited }

            recyclerViewLostItems.adapter = ItemsAdapter(lostItems) { item -> openItemDetail(item) }
            recyclerViewFoundItems.adapter = ItemsAdapter(foundItems) { item -> openItemDetail(item) }
            recyclerViewReunitedItems.adapter = ItemsAdapter(reunitedItems) { item -> openItemDetail(item) }
        })

        return view
    }

    private fun openItemDetail(item: Items) {
        val intent = Intent(activity, ItemDetailActivity::class.java).apply {
            putExtra("ITEM_ID", item.itemId)
        }
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        itemsViewModel.loadItems() // Reload items when fragment becomes visible
    }
}