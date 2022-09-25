package com.example.ugd_3_kelompok

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd_3_kelompok.entity.Kamar

class HomeFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home,container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        val adapter: RVKamar = RVKamar(Kamar.userKamar)

        val rvData : RecyclerView = view.findViewById(R.id.rv_kamar)

        rvData.layoutManager = layoutManager

        rvData.setHasFixedSize(true)

        rvData.adapter = adapter
    }
}