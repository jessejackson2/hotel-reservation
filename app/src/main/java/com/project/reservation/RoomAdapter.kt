package com.project.reservation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

// Adapter for displaying Room objects in a RecyclerView
class RoomAdapter(private val rooms: List<Room>, private val reserveListener: (Int) -> Unit) :
    RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.room_item, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.bind(room)
    }

    override fun getItemCount(): Int = rooms.size

    // ViewHolder for displaying a Room object in a RecyclerView
    inner class RoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(room: Room) {
            itemView.findViewById<TextView>(R.id.roomType).text = room.type
            itemView.findViewById<TextView>(R.id.roomPrice).text = room.price
            itemView.findViewById<TextView>(R.id.roomDescription).text = room.description

            // Load image using the resource ID
            itemView.findViewById<ImageView>(R.id.roomImage).setImageResource(room.imageResId)

            itemView.findViewById<Button>(R.id.reserveButton).setOnClickListener {
                reserveListener(room.id)
            }
        }
    }

}
