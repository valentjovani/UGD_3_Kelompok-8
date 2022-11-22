package com.example.ugd_3_kelompok

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class MemberHotelFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_member_hotel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnAdd: Button = view.findViewById(R.id.btnAdd)


        btnAdd.setOnClickListener(View.OnClickListener {
            val moveMemberHotel = Intent(this@MemberHotelFragment.context, MemberHotelActivity::class.java)
            startActivity(moveMemberHotel)
        })
    }


}