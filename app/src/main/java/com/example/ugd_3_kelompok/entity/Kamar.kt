package com.example.ugd_3_kelompok.entity

class Kamar(
    var jenis: String
) {

    companion object{
        @JvmField
        var userWorkout = arrayOf(
            Kamar("Jumping Jacks"),
            Kamar("Russian Twist"),
            Kamar("Push Up"),
            Kamar("Heel Touch"),
            Kamar("Sit Up"),
            Kamar("Chin Up"),
            Kamar("Squats"),
            Kamar("Climbers"),
            Kamar("Leg Raises"),
        )
    }
}