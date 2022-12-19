package com.example.ugd_3_kelompok

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
import com.example.ugd_3_kelompok.models.PemesananKamarModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList

class PemesananKamarAdapter (private var pemesananKamarList : List<PemesananKamarModel>, context: Context
) :
    RecyclerView.Adapter<PemesananKamarAdapter.ViewHolder>(), Filterable {
    private var filteredKamarList: MutableList<PemesananKamarModel>
    private val context: Context

    init{
        filteredKamarList = ArrayList(pemesananKamarList)
        this.context=context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_pelanggan, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredKamarList.size
    }

    fun setPemesananKamarList(pemesananKamarList: Array<PemesananKamarModel>){
        this.pemesananKamarList = pemesananKamarList.toList()
        filteredKamarList = pemesananKamarList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val kamar = filteredKamarList[position]
        holder.tvJenisKamar.text = kamar.jenisKamar
        holder.tvTanggal.text = kamar.tanggal
        holder.tvDurasi.text = kamar.durasi

        holder.btnDelete.setOnClickListener{
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data Pemesanan kamar ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus"){_, _ ->
                    if(context is PemesananKamarActivity){
                        context.deletePemesanaKamar(
                            kamar.id
                        )
                    }
                }
                .show()
        }
        holder.cvMahasiswa.setOnClickListener{
            val i = Intent(context, EditPemesananKamar::class.java)
            i.putExtra("id",kamar.id)
            if(context is PemesananKamarActivity)
                context.startActivityForResult(i, PemesananKamarActivity.LAUNCH_ADD_ACTIVITY)

        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered : MutableList<PemesananKamarModel> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(pemesananKamarList)
                }else{
                    for(kamar in pemesananKamarList){
                        if(kamar.jenisKamar.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) filtered.add(kamar)

                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredKamarList.clear()
                filteredKamarList.addAll((filterResults.values as List<PemesananKamarModel>))
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvJenisKamar: TextView
        var tvTanggal: TextView
        var tvDurasi: TextView
        var btnDelete: ImageButton
        var cvMahasiswa: CardView

        init{
            tvJenisKamar = itemView.findViewById(R.id.tvJenisKamar)
            tvTanggal = itemView.findViewById(R.id.tvTanggal)
            tvDurasi = itemView.findViewById(R.id.tvDurasi)
            btnDelete = itemView.findViewById(R.id.btn_delete)
            cvMahasiswa = itemView.findViewById(R.id.cv_pelanggan)
        }
    }
}