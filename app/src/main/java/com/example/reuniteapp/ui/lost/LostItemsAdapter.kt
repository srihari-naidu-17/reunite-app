package com.example.reuniteapp.ui.lost

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reuniteapp.R
import com.example.reuniteapp.models.LostItems

class LostItemsAdapter(private val lostItemsList: List<LostItems>) : RecyclerView.Adapter<LostItemsAdapter.LostItemViewHolder>() {

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