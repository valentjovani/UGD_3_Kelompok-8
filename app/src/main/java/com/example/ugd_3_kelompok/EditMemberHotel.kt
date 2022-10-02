package com.example.ugd_3_kelompok

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.ugd_3_kelompok.room.Constant
import com.example.ugd_3_kelompok.room.MemberHotel
import com.example.ugd_3_kelompok.room.MemberHotelDB
import kotlinx.android.synthetic.main.activity_edit_member_hotel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.ugd_3_kelompok.databinding.ActivityEditMemberHotelBinding


class EditMemberHotel : AppCompatActivity() {
    val db by lazy { MemberHotelDB(this) }
    private var memberHotelId : Int = 0
    private var binding: ActivityEditMemberHotelBinding? = null
    private val MEMBER_HOTEL_1 = "notif_member_hotel_1"
    private val notificationId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_member_hotel)
        supportActionBar?.hide()
        setupView()
        setupListener()
    }

    fun setupView() {
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType){
            Constant.TYPE_CREATE-> {
                btnUpdate.visibility = View.GONE
            }
            Constant.TYPE_READ-> {
                btnSave.visibility = View.GONE
                btnUpdate.visibility = View.GONE
                getMemberHotel()
            }
            Constant.TYPE_UPDATE-> {
                btnSave.visibility = View.GONE
                getMemberHotel()
            }
        }
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(MEMBER_HOTEL_1, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }


            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
        }
    }

    private fun sendNotification2() {
        val logo = BitmapFactory.decodeResource(resources, R.drawable.kamar)
        val builder = NotificationCompat.Builder(this, MEMBER_HOTEL_1)
            .setSmallIcon(R.drawable.kamar)
            .setContentTitle("Edit Member Hotel")
            .setContentText("Success Edit as "+ binding?.etMembership?.text.toString())
            .setColor(Color.BLUE)
            .setLargeIcon(logo)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigLargeIcon(null)
                    .bigPicture(logo)
            )
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }

    private fun sendNotification1() {
        val logo = BitmapFactory.decodeResource(resources, R.drawable.kamar)
        val builder = NotificationCompat.Builder(this, MEMBER_HOTEL_1)
            .setSmallIcon(R.drawable.kamar)
            .setContentTitle("Add Member Hotel")
            .setContentText("Success Add as "+ binding?.etMembership?.text.toString())
            .setColor(Color.BLUE)
            .setLargeIcon(logo)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigLargeIcon(null)
                    .bigPicture(logo)
            )
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }

    private fun setupListener() {
        btnSave.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.MemberHotelDao().addMemberHotel(
                    MemberHotel(
                        0, etFasilitas.text.toString(),
                        etMembership.text.toString(),
                        etTanggal.text.toString(),
                        etDurasi.text.toString()
                    )
                )
                createNotificationChannel()
                sendNotification1()
                finish()
            }
        }
        btnUpdate.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.MemberHotelDao().updateMemberHotel(
                    MemberHotel(memberHotelId,
                        etFasilitas.text.toString(),
                        etMembership.text.toString(),
                        etTanggal.text.toString(),
                        etDurasi.text.toString())
                )
                createNotificationChannel()
                sendNotification2()
                finish()
            }
        }
    }
    fun getMemberHotel(){
        memberHotelId = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val memberHotel = db.MemberHotelDao().getMemberHotel(memberHotelId)[0]
            etFasilitas.setText(memberHotel.fasilitas)
            etMembership.setText(memberHotel.membership)
            etTanggal.setText(memberHotel.tanggal)
            etDurasi.setText(memberHotel.durasi)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}