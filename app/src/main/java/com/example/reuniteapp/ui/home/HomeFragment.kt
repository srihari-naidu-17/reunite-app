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
import com.example.reuniteapp.ui.report.ReportActivity
import com.example.reuniteapp.ui.ItemsAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var addReportButton: FloatingActionButton
    private lateinit var recyclerViewItems: RecyclerView

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

        // Initialize add report button
        addReportButton = view.findViewById(R.id.addReportButton)

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

        // Initialize RecyclerView
        recyclerViewItems = view.findViewById(R.id.recyclerViewItems)
        recyclerViewItems.layoutManager = LinearLayoutManager(requireContext())

        // Observe items LiveData
        itemsViewModel.items.observe(viewLifecycleOwner, Observer { items ->
            recyclerViewItems.adapter = ItemsAdapter(items) { item ->
                // Handle item click if needed
            }
        })

        // Set up add report button listener
        addReportButton.setOnClickListener {
            val intent = Intent(activity, ReportActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        itemsViewModel.loadItems() // Reload items when fragment becomes visible
    }
}
