package com.example.reuniteapp.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reuniteapp.R
import com.example.reuniteapp.models.Items

class NotificationsAdapter(
    private val notifications: List<Items>
) : RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.notificationMessage)
        val timestampTextView: TextView = itemView.findViewById(R.id.notificationTimestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.messageTextView.text = when {
            notification.reunited -> "Reunited: ${notification.itemTitle}"
            notification.itemCategory == "Lost Item" -> "Lost Item: ${notification.itemTitle}"
            notification.itemCategory == "Found Item" -> "Found Item: ${notification.itemTitle}"
            else -> notification.itemTitle
        }
        holder.timestampTextView.text = "${notification.date} ${notification.time}"
    }

    override fun getItemCount() = notifications.size
}