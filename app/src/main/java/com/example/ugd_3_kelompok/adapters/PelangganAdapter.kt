package com.example.ugd_3_kelompok.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd_3_kelompok.AddEditPelangganActivity
import com.example.ugd_3_kelompok.RetrofitPelanggan
import com.example.ugd_3_kelompok.R
import com.example.ugd_3_kelompok.models.Pelanggan
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList

class PelangganAdapter(private var  pelangganList: List<Pelanggan>, context: Context) :
    RecyclerView.Adapter<PelangganAdapter.ViewHolder>(), Filterable {
        private var filteredPelangganList: MutableList<Pelanggan>
        private var context: Context

        init {
            filteredPelangganList = ArrayList(pelangganList)
            this.context = context
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var tvNama: TextView
            var tvUmur: TextView
            var tvNotelp: TextView
            var tvGender: TextView
            var btnDelete: ImageButton
            var cvPelanggan: CardView

            init {
                tvNama = itemView.findViewById(R.id.tv_nama)
                tvUmur = itemView.findViewById(R.id.tv_umur)
                tvNotelp = itemView.findViewById(R.id.tv_notelp)
                tvGender = itemView.findViewById(R.id.tv_gender)
                btnDelete = itemView.findViewById(R.id.btn_delete)
                cvPelanggan = itemView.findViewById(R.id.cv_pelanggan)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.item_pelanggan, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val pelanggan = filteredPelangganList[position]
            holder.tvNama.text = pelanggan.nama
            holder.tvUmur.text = pelanggan.umur
            holder.tvNotelp.text = pelanggan.notelp
            holder.tvGender.text = pelanggan.gender

            holder.btnDelete.setOnClickListener {
                val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
                materialAlertDialogBuilder.setTitle("konfirmasi")
                    .setMessage("Apakah anda yakin ingin menghapus data pelanggan ini?")
                    .setNegativeButton("Batal", null)
                    .setPositiveButton("Hapus") { _, _ ->
                        if (context is RetrofitPelanggan) pelanggan.id?.let { it1 ->
                            (context as RetrofitPelanggan).deletePelanggan(
                                it1
                            )
                        }
                    }
                    .show()
            }
            holder.cvPelanggan.setOnClickListener {
                val i = Intent(context, AddEditPelangganActivity::class.java)
                i.putExtra("id", pelanggan.id)
                if (context is RetrofitPelanggan)
                    (context as RetrofitPelanggan).startActivityForResult(i, RetrofitPelanggan.LUNCH_ADD_ACTIVITY)
            }
        }

        override fun getItemCount(): Int {
            return filteredPelangganList.size
        }

        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(charSequence: CharSequence): FilterResults {
                    val charSequenceString = charSequence.toString()
                    val filtered:MutableList<Pelanggan> = java.util.ArrayList()
                    if (charSequenceString.isEmpty()) {
                        filtered.addAll(pelangganList)
                    } else {
                        for (pelanggan in pelangganList) {
                            if (pelanggan.nama.lowercase(Locale.getDefault())
                                    .contains(charSequenceString.lowercase(Locale.getDefault()))
                            ) filtered.add(pelanggan)
                        }
                    }

                    val filterResult = FilterResults()
                    filterResult.values = filtered
                    return filterResult
                }

                override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                    filteredPelangganList.clear()
                    filteredPelangganList.addAll((filterResults.values as List<Pelanggan>))
                    notifyDataSetChanged()
                }
            }
        }

        fun setMahasiswaList(pelangganList: Array<Pelanggan>) {
            this.pelangganList = pelangganList.toList()
            filteredPelangganList = pelangganList.toMutableList()
        }
    }

