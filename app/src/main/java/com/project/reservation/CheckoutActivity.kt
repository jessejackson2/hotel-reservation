package com.project.reservation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CheckoutActivity : AppCompatActivity() {
    //initialize variables and views
    private val channelId = "checkout_notifications"
    private lateinit var roomTypeTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var completeButton: Button
    private lateinit var roomImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_checkout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        createNotificationChannel()
        // Initialize views
        roomTypeTextView = findViewById(R.id.checkoutRoomType)
        dateTextView = findViewById(R.id.checkoutDate)
        priceTextView = findViewById(R.id.checkoutPrice)
        roomImageView = findViewById(R.id.roomImageView)
        completeButton = findViewById(R.id.completeButton)

        // Fetch data from intent
        val roomType = intent.getStringExtra("ROOM_TYPE") ?: "N/A"
        val price = intent.getStringExtra("PRICE") ?: "N/A"
        val date = intent.getStringExtra("DATE") ?: "N/A"
        val imageResId = intent.getIntExtra("IMAGE_RES_ID", -1)

        // Set data in views
        roomTypeTextView.text = roomType
        dateTextView.text = date
        priceTextView.text = price
        if (imageResId != -1) {
            roomImageView.setImageResource(imageResId)
        }

        // Set click listener for completion of reservation
        completeButton.setOnClickListener {
            Toast.makeText(this, "Reservation Completed!", Toast.LENGTH_SHORT).show()
            showNotification(
                "Reservation Completed",
                "Your reservation for $roomType on $date has been completed."
            )
            finish()
        }
    }

    //create notification channel
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Checkout Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Channel for reservation notifications"
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    //show notification
    private fun showNotification(title: String, message: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.hotel)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}