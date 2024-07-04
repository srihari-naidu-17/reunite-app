package com.example.reuniteapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reuniteapp.R
import com.example.reuniteapp.models.Items

class ItemsAdapter(
    private val items: List<Items>,
    private val itemClickListener: (Items) -> Unit
) : RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemTitle: TextView = itemView.findViewById(R.id.itemTitle)
        private val itemDescription: TextView = itemView.findViewById(R.id.itemDescription)
        private val itemLocation: TextView = itemView.findViewById(R.id.itemLocation)
        private val itemCategory: TextView = itemView.findViewById(R.id.itemCategory)

        fun bind(item: Items) {
            itemTitle.text = item.itemTitle
            itemDescription.text = item.itemDescription
            itemLocation.text = item.location
            itemCategory.text = item.itemCategory

            itemView.setOnClickListener {
                itemClickListener(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
