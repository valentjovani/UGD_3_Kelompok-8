package com.example.ugd_3_kelompok.entity

class Pelanggan(
    var nama: String, var umur: Int,
    var noTelp: String, var gender: String
) {

    companion object{
        @JvmField
        var userData = arrayOf(
            Pelanggan("Agung Wibu",22,"085617384628","Laki-laki"),
            Pelanggan("Cahyani Ningsih",18,"087875894023","Perempuan"),
            Pelanggan("Nico Santoso",23,"081789431263","Laki-laki"),
            Pelanggan("Violet Evergarden",19,"085728583920","Perempuan"),
            Pelanggan("Tobichi Origami",20,"082675893047","Perempuan"),
            Pelanggan("Darell Coliers",21,"083785930467","Laki-laki"),
            Pelanggan("Alvin Nugraha",17,"082637890456","Laki-laki"),
            Pelanggan("Bronya Zachik",19,"086749273890","Perempuan"),
            Pelanggan("Seele Volerei",25,"081223471923","Perempuan"),
            Pelanggan("Elysia",22,"083562789456","Perempuan"),
        )
    }
}