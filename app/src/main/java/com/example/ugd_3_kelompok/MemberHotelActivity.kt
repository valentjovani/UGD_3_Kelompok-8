package com.example.ugd_3_kelompok

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ugd_3_kelompok.room.Constant
import com.example.ugd_3_kelompok.room.MemberHotel
import com.example.ugd_3_kelompok.room.MemberHotelDB
import kotlinx.android.synthetic.main.activity_member_hotel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MemberHotelActivity : AppCompatActivity() {
    val db by lazy{ MemberHotelDB(this) }
    lateinit var memberHotelAdapter: MemberHotelAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_hotel)
        supportActionBar?.hide()
        setupListener()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        memberHotelAdapter = MemberHotelAdapter(arrayListOf(), object :
            MemberHotelAdapter.OnAdapterListener {
            override fun onClick(memberHotel : MemberHotel) {
                Toast.makeText(applicationContext, memberHotel.fasilitas,
                    Toast.LENGTH_SHORT).show()
                intentEdit(memberHotel.id, Constant.TYPE_READ)
            }
            override fun onUpdate(memberHotel: MemberHotel) {
                intentEdit(memberHotel.id, Constant.TYPE_UPDATE)
            }
            override fun onDelete(memberHotel : MemberHotel) {
                deleteDialog(memberHotel)
            }
        })
        list_hotel.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = memberHotelAdapter
        }
    }

    private fun deleteDialog(memberHotel: MemberHotel){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Confirmation")
            setMessage("Are You Sure to delete this data From ${memberHotel.fasilitas}?")
            setNegativeButton("Cancel", DialogInterface.OnClickListener
            { dialogInterface, i ->
                dialogInterface.dismiss()
            })
            setPositiveButton("Delete", DialogInterface.OnClickListener
            { dialogInterface, i ->
                dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    db.MemberHotelDao().deleteMemberHotel(memberHotel)
                    loadData()
                }
            })
        }
        alertDialog.show()
    }
    override fun onStart() {
        super.onStart()
        loadData()
    }

    fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            val memberHotel = db.MemberHotelDao().getMemberHotel()
            Log.d("MemberHotelActivity","dbResponse: $memberHotel")
            withContext(Dispatchers.Main){
                memberHotelAdapter.setData( memberHotel )
            }
        }
    }
    fun setupListener() {
        btnAdd.setOnClickListener{
            intentEdit(0,Constant.TYPE_CREATE)
        }
    }
    fun intentEdit(memberHotelId : Int, intentType: Int){
        startActivity(
            Intent(applicationContext, EditMemberHotel::class.java)
                .putExtra("intent_id", memberHotelId)
                .putExtra("intent_type", intentType)
        )
    }
}