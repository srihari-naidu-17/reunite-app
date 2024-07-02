package com.example.reuniteapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.example.reuniteapp.R
import com.example.reuniteapp.ui.report.ReportItemActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var addLostitemFab: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize search view
        searchView = view.findViewById(R.id.searchView)

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

        // Initialize Floating Action Button
        val addLostItemButton: FloatingActionButton = view.findViewById(R.id.addLostItemButton)

        // Set up click listener for FAB
        addLostItemButton.setOnClickListener {
            showPopupMenu(addLostItemButton)
        }

        return view
    }

    private fun showPopupMenu(view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(R.menu.popup_menu, popup.menu)

        popup.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.menu_report_lost_item -> {
                    // Handle report lost item
                    navigateToReportItem("lost")
                    true
                }
                R.id.menu_report_found_item -> {
                    // Handle report found item
                    navigateToReportItem("found")
                    true
                }
                else -> false
            }
        }

        popup.show()
    }

    private fun navigateToReportItem(itemType: String) {
        val intent = Intent(requireContext(), ReportItemActivity::class.java)
        intent.putExtra("itemType", itemType)
        startActivity(intent)
    }
}
