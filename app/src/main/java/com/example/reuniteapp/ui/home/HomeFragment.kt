package com.example.reuniteapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reuniteapp.R
import com.example.reuniteapp.models.FoundItems
import com.example.reuniteapp.models.LostItems
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.TextView

class HomeFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var lostItemsRecyclerView: RecyclerView
    private lateinit var foundItemsRecyclerView: RecyclerView
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

        // Initialize RecyclerViews
        lostItemsRecyclerView = view.findViewById(R.id.LostItemRecyclerView)
        foundItemsRecyclerView = view.findViewById(R.id.FoundItemRecyclerView)

        // Set up Lost Item RecyclerView
        lostItemsRecyclerView.layoutManager = LinearLayoutManager(context)
        lostItemsRecyclerView.adapter = LostItemsAdapter(getSampleLostItems())

        // Set up Found Item RecyclerView
        foundItemsRecyclerView.layoutManager = LinearLayoutManager(context)
        foundItemsRecyclerView.adapter = FoundItemsAdapter(getSampleFoundItems())

        // Set up FloatingActionButton
        addLostItemButton = view.findViewById(R.id.addLostItemButton)
        addLostItemButton.setOnClickListener {
            // Handle add lost item button click
        }

        return view
    }

    private fun getSampleLostItems(): List<LostItems> {
        return listOf(
            LostItems(1, "Description 1", "01-01-2023", "Location 1", "Contact 1"),
            LostItems(2, "Description 2", "02-01-2023", "Location 2", "Contact 2")
        )
    }

    private fun getSampleFoundItems(): List<FoundItems> {
        return listOf(
            FoundItems(1, "Description 1", "01-01-2023", "Location 1", "Contact 1"),
            FoundItems(2, "Description 2", "02-01-2023", "Location 2", "Contact 2")
        )
    }

    private class LostItemsAdapter(private val lostItemsList: List<LostItems>) : RecyclerView.Adapter<LostItemsAdapter.LostItemViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LostItemViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_lost, parent, false)
            return LostItemViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: LostItemViewHolder, position: Int) {
            val currentItem = lostItemsList[position]
            holder.nameTextView.text = currentItem.name
            holder.descriptionTextView.text = currentItem.description
            holder.dateLostTextView.text = currentItem.dateLost
            holder.locationLostTextView.text = currentItem.locationLost
            holder.contactInfoTextView.text = currentItem.contactInfo
        }

        override fun getItemCount() = lostItemsList.size

        class LostItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nameTextView: TextView = itemView.findViewById(R.id.LostName)
            val descriptionTextView: TextView = itemView.findViewById(R.id.LostDescription)
            val dateLostTextView: TextView = itemView.findViewById(R.id.LostDate)
            val locationLostTextView: TextView = itemView.findViewById(R.id.LostLocation)
            val contactInfoTextView: TextView = itemView.findViewById(R.id.LostContactInfo)
        }
    }

    private class FoundItemsAdapter(private val foundItemsList: List<FoundItems>) : RecyclerView.Adapter<FoundItemsAdapter.FoundItemViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoundItemViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_found, parent, false)
            return FoundItemViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: FoundItemViewHolder, position: Int) {
            val currentItem = foundItemsList[position]
            holder.nameTextView.text = currentItem.name
            holder.descriptionTextView.text = currentItem.description
            holder.dateFoundTextView.text = currentItem.dateFound
            holder.locationFoundTextView.text = currentItem.locationFound
            holder.contactInfoTextView.text = currentItem.contactInfo
        }

        override fun getItemCount() = foundItemsList.size

        class FoundItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
            val descriptionTextView: TextView = itemView.findViewById(R.id.FoundDescription)
            val dateFoundTextView: TextView = itemView.findViewById(R.id.FoundDate)
            val locationFoundTextView: TextView = itemView.findViewById(R.id.FoundLocation)
            val contactInfoTextView: TextView = itemView.findViewById(R.id.FoundContactInfo)
        }
    }

}