package com.project.reservation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import java.text.SimpleDateFormat
import java.util.*

// Room data class
data class Room(
    val id: Int,
    val type: String,
    val price: String,
    val description: String,
    val imageResId: Int
)

class MainActivity : AppCompatActivity() {

    // List of rooms for the recycler view
    private val rooms = listOf(
        Room(
            1,
            "Single Room",
            "$1000",
            "This is a single room with a view of the beach.",
            R.drawable.single_room
        ),
        Room(
            2,
            "Double Room",
            "$1500",
            "This is a double room with modern amenities.",
            R.drawable.double_room
        ),
        Room(
            3,
            "Family Room",
            "$2000",
            "This is a family room with spacious living areas.",
            R.drawable.family_room
        ),
        Room(
            4,
            "King Room",
            "$2500",
            "This is a king room with luxurious amenities.",
            R.drawable.king_room
        ),
        Room(
            5,
            "Twin Room",
            "$3000",
            "This is a twin room offering a great view of the city.",
            R.drawable.twin_room
        ),
        Room(6, "Studio", "$3500", "This is a studio room with a balcony.", R.drawable.studio)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RoomAdapter(rooms) { roomId ->
            onReserveClicked(roomId)
        }

        // Check and request notification permission
        checkNotificationPermission()

    }

    // Handle reserve button click
    private fun onReserveClicked(roomId: Int) {
        val selectedRoom = rooms.find { it.id == roomId } ?: return

        // Show DatePickerDialog
        val calendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            // Check if the date is in the past
            val selectedDate = calendar.time
            val currentDate = Calendar.getInstance().time

            if (selectedDate.before(currentDate)) {
                Toast.makeText(this, "Dates can only be in the future", Toast.LENGTH_SHORT).show()
                return@OnDateSetListener // Exit the listener
            }

            // Proceed if the date is valid
            val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(selectedDate)
            showConfirmationDialog(selectedRoom, formattedDate)
        }

        DatePickerDialog(
            this, dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    // Show confirmation dialog
    private fun showConfirmationDialog(room: Room, date: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Reservation")
        builder.setMessage("Room: ${room.type}\nDate: $date\nPrice: ${room.price}")

        builder.setPositiveButton("Go to Checkout") { _, _ ->
            // Navigate to CheckoutActivity
            val intent = Intent(this, CheckoutActivity::class.java).apply {
                putExtra("ROOM_TYPE", room.type)
                putExtra("PRICE", room.price)
                putExtra("DATE", date)
                putExtra("IMAGE_RES_ID", room.imageResId)
            }
            startActivity(intent)
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    // Check and request notification permission
    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Check if the notification permission is granted
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request the permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // ActivityResultLauncher for requesting permissions
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }

}