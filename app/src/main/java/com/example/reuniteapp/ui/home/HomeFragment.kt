package com.example.reuniteapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import com.example.reuniteapp.R
import com.example.reuniteapp.ui.AddItemActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var addLostItemButton: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize search view
        searchView = view.findViewById(R.id.searchView)

        // Initialize add lost item button
        addLostItemButton = view.findViewById(R.id.addLostItemButton)

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

        // Set up add lost item button listener
        addLostItemButton.setOnClickListener {
            val intent = Intent(activity, AddItemActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}
