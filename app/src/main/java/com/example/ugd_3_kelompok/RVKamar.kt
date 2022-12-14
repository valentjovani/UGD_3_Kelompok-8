package com.example.ugd_3_kelompok

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd_3_kelompok.entity.Kamar

class RVKamar(private val data: Array<Kamar>) : RecyclerView.Adapter<RVKamar.viewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_kamar,parent,false)
        return viewHolder(itemView)
    }


    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currentItem = data[position]
        holder.tvKamar.text = currentItem.jenis

    }

    override fun getItemCount(): Int {
        return data.size
    }

    class viewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvKamar : TextView = itemView.findViewById(R.id.jenisKamar)
    }
}