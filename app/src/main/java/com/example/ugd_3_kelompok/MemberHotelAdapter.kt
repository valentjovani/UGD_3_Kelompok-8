package com.example.ugd_3_kelompok

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd_3_kelompok.room.MemberHotel
import kotlinx.android.synthetic.main.activity_member_hotel_adapter.view.*

class MemberHotelAdapter (private val memberHotels : ArrayList<MemberHotel>, private val
listener: OnAdapterListener
) :
    RecyclerView.Adapter<MemberHotelAdapter.MemberHotelViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            MemberHotelViewHolder {
        return MemberHotelViewHolder(

            LayoutInflater.from(parent.context).inflate(R.layout.activity_member_hotel_adapter, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: MemberHotelViewHolder, position:
        Int ) {
        val memberHotel = memberHotels[position]
        holder.view.tvFasilitas.text = memberHotel.fasilitas
        holder.view.tvMembership.text = memberHotel.membership
        holder.view.tvTanggal.text = memberHotel.tanggal
        holder.view.tvDurasi.text = memberHotel.durasi
        holder.view.tvFasilitas.setOnClickListener {
            listener.onClick(memberHotel)
        }
        holder.view.icon_edit.setOnClickListener {
            listener.onUpdate(memberHotel)
        }
        holder.view.icon_delete.setOnClickListener {
            listener.onDelete(memberHotel)
        }
    }

    override fun getItemCount() = memberHotels.size
    inner class MemberHotelViewHolder(val view: View) :
        RecyclerView.ViewHolder(view)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<MemberHotel>) {
        memberHotels.clear()
        memberHotels.addAll(list)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onClick(memberHotel: MemberHotel)
        fun onUpdate(memberHotel : MemberHotel)
        fun onDelete(memberHotel: MemberHotel)

    }
}