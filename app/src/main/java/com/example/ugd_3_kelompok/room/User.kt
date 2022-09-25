package com.example.ugd_3_kelompok.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity

data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val username : String,
    val password : String,
    val email : String,
    val tanggalLahir : String,
    val nomorTelepon : String
)