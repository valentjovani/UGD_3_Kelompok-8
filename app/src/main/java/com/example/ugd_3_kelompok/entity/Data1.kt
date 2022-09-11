package com.example.ugd_3_kelompok.entity

class Data1(var name: String, var pengajar: String) {
    companion object{
        @JvmField
        var listOfData1 = arrayOf(
            Data1("Fedelis Brian", "Pengajar Kelas A, B, dan D"),
            Data1("Thomas Adi", "Pengajar kelas C")
        )
    }
}