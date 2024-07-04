package com.example.reuniteapp.ui.found

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reuniteapp.R
import com.example.reuniteapp.models.FoundItems

class FoundItemsAdapter(private val foundItemsList: List<FoundItems>) : RecyclerView.Adapter<FoundItemsAdapter.FoundItemViewHolder>() {

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
        val nameTextView: TextView = itemView.findViewById(R.id.FoundName)
        val descriptionTextView: TextView = itemView.findViewById(R.id.FoundDescription)
        val dateFoundTextView: TextView = itemView.findViewById(R.id.FoundDate)
        val locationFoundTextView: TextView = itemView.findViewById(R.id.FoundLocation)
        val contactInfoTextView: TextView = itemView.findViewById(R.id.FoundContactInfo)
    }
}