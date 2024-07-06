package com.example.reuniteapp.ui

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reuniteapp.R
import com.example.reuniteapp.models.Items
import java.io.File
import java.io.FileInputStream

class ItemsAdapter(
    private val items: List<Items>,
    private val itemClickListener: (Items) -> Unit
) : RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View, private val context: Context) : RecyclerView.ViewHolder(itemView) {
        private val itemTitle: TextView = itemView.findViewById(R.id.itemTitle)
        private val itemDescription: TextView = itemView.findViewById(R.id.itemDescription)
        private val itemLocation: TextView = itemView.findViewById(R.id.itemLocation)
        private val itemCategory: TextView = itemView.findViewById(R.id.itemCategory)
        private val itemImage: ImageView = itemView.findViewById(R.id.itemImage)

        fun bind(item: Items) {
            itemTitle.text = item.itemTitle
            itemDescription.text = item.itemDescription
            itemLocation.text = item.location
            itemCategory.text = item.itemCategory

            // Load the image from internal storage
            val filePath = context.applicationContext.filesDir.path + "/images/" + item.itemImage
            val file = File(filePath)
            if (file.exists()) {
                val bitmap = BitmapFactory.decodeStream(FileInputStream(file))
                itemImage.setImageBitmap(bitmap)
            }

            itemView.setOnClickListener {
                itemClickListener(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
