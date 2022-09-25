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

class EditMemberHotel : AppCompatActivity() {
    val db by lazy { MemberHotelDB(this) }
    private var memberHotelId : Int = 0

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