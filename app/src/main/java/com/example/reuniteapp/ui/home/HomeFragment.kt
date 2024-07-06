package com.example.reuniteapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reuniteapp.R
import com.example.reuniteapp.models.Items
import com.example.reuniteapp.ui.ItemsAdapter
import com.example.reuniteapp.ui.ItemDetailActivity

class HomeFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var recyclerViewLostItems: RecyclerView
    private lateinit var recyclerViewFoundItems: RecyclerView

    private val itemsViewModel: ItemsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize search view
        searchView = view.findViewById(R.id.searchView)

        // Initialize RecyclerViews
        recyclerViewLostItems = view.findViewById(R.id.recyclerViewLostItems)
        recyclerViewFoundItems = view.findViewById(R.id.recyclerViewFoundItems)
        recyclerViewLostItems.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewFoundItems.layoutManager = LinearLayoutManager(requireContext())

        // Observe items LiveData
        itemsViewModel.items.observe(viewLifecycleOwner, Observer { items ->
            val lostItems = items.filter { !it.reunited && it.itemCategory == "Lost Item" }
            val foundItems = items.filter { !it.reunited && it.itemCategory == "Found Item" }

            recyclerViewLostItems.adapter = ItemsAdapter(lostItems) { item ->
                openItemDetail(item)
            }

            recyclerViewFoundItems.adapter = ItemsAdapter(foundItems) { item ->
                openItemDetail(item)
            }
        })

        // Set up search view listeners
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search query submission
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle search query text change
                return true
            }
        })

        return view
    }

    override fun onResume() {
        super.onResume()
        itemsViewModel.loadItems() // Reload items when fragment becomes visible
    }

    private fun openItemDetail(item: Items) {
        val intent = Intent(activity, ItemDetailActivity::class.java).apply {
            putExtra("ITEM_ID", item.itemId)
        }
        startActivity(intent)
    }
}
