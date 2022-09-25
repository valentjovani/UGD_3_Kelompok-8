package com.example.ugd_3_kelompok.entity

class Kamar(
    var jenis: String
) {

    companion object{
        @JvmField
        var userWorkout = arrayOf(
            Kamar("Standard Room"),
            Kamar("Superior Room"),
            Kamar("Deluxe Room"),
            Kamar("Junior Suite Room"),
            Kamar("Suite Room"),
            Kamar("Presidential Suite"),
            Kamar("Single Room"),
            Kamar("Twin Room"),
            Kamar("Double Room"),
            Kamar("Family Room"),
            Kamar("Connecting Room"),
            Kamar("Murphy Room"),
            Kamar("Accessible/Disabled Room"),
            Kamar("Smoking/Non Smoking Room"),
            Kamar("Cabana Room"),
        )
    }
}