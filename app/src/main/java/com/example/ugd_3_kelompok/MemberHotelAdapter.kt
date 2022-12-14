package com.example.ugd_3_kelompok

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd_3_kelompok.models.MemberHotelModel
import com.example.ugd_3_kelompok.room.MemberHotel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_member_hotel_adapter.view.*
import java.util.*
import kotlin.collections.ArrayList

class MemberHotelAdapter (private var memberHotelList : List<MemberHotelModel>, context: Context
) :
    RecyclerView.Adapter<MemberHotelAdapter.ViewHolder>(), Filterable {
    private var filteredMemberList: MutableList<MemberHotelModel>
    private val context: Context

    init{
        filteredMemberList = ArrayList(memberHotelList)
        this.context=context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_pelanggan, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredMemberList.size
    }

    fun setMemberHotelList(memberHotelList: Array<MemberHotelModel>){
        this.memberHotelList = memberHotelList.toList()
        filteredMemberList = memberHotelList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val member = filteredMemberList[position]
        holder.tvFasilitas.text = member.fasilitas
        holder.tvMembership.text = member.membership
        holder.tvTanggal.text = member.tanggal
        holder.tvDurasi.text = member.durasi

        holder.btnDelete.setOnClickListener{
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data Member Hotel ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus"){_, _ ->
                    if(context is MemberHotelActivity){
                        context.deleteMemberHotel(
                            member.id
                        )
                    }
                }
                .show()
        }
        holder.cvMahasiswa.setOnClickListener{
            val i = Intent(context, EditMemberHotel::class.java)
            i.putExtra("id",member.id)
            if(context is MemberHotelActivity)
                context.startActivityForResult(i, MemberHotelActivity.LAUNCH_ADD_ACTIVITY)

        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered : MutableList<MemberHotelModel> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(memberHotelList)
                }else{
                    for(member in memberHotelList){
                        if(member.fasilitas.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) filtered.add(member)

                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredMemberList.clear()
                filteredMemberList.addAll((filterResults.values as List<MemberHotelModel>))
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvFasilitas: TextView
        var tvMembership: TextView
        var tvTanggal: TextView
        var tvDurasi: TextView
        var btnDelete: ImageButton
        var cvMahasiswa: CardView

        init{
            tvFasilitas = itemView.findViewById(R.id.tvFasilitas)
            tvMembership = itemView.findViewById(R.id.tvMembership)
            tvTanggal = itemView.findViewById(R.id.tvTanggal)
            tvDurasi = itemView.findViewById(R.id.tvDurasi)
            btnDelete = itemView.findViewById(R.id.btn_delete)
            cvMahasiswa = itemView.findViewById(R.id.cv_pelanggan)
        }
    }
}
