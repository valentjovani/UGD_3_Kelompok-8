package com.example.ugd_3_kelompok

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd_3_kelompok.room.MemberHotel
import kotlinx.android.synthetic.main.activity_member_gym_adapter.view.*

class MemberHotelAdapter (private val memberGyms : ArrayList<MemberHotel>, private val
listener: OnAdapterListener
) :
    RecyclerView.Adapter<MemberHotelAdapter.MemberHotelViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            MemberGymViewHolder {
        return MemberGymViewHolder(

            LayoutInflater.from(parent.context).inflate(R.layout.activity_member_hotel_adapter, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: MemberGymViewHolder, position:
        Int ) {
        val memberGym = memberGyms[position]
        holder.view.tvPersonalTrainer.text = memberGym.personalTrainer
        holder.view.tvMembership.text = memberGym.membership
        holder.view.tvTanggal.text = memberGym.tanggal
        holder.view.tvDurasi.text = memberGym.durasi
        holder.view.tvPersonalTrainer.setOnClickListener {
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
    inner class MemberGymViewHolder(val view: View) :
        RecyclerView.ViewHolder(view)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<MemberHotel>) {
        memberGyms.clear()
        memberGyms.addAll(list)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onClick(memberHotel: MemberHotel)
        fun onUpdate(memberHotel : MemberHotel)
        fun onDelete(memberHotel: MemberHotel)

    }
}