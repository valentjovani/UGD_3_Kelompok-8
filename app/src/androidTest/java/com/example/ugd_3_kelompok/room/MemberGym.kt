package com.example.ugd_3_kelompok.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity

data class MemberGym (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val personalTrainer: String,
    val membership: String,
    val tanggal: String,
    val durasi: String
)