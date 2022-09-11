package com.example.ugd_3_kelompok.entity

class Data2(var name: String, var IPK: Double, var tahunMasuk: Int) {
    companion object{
        @JvmField
        var listOfData2 = arrayOf(
            Data2("Wendy Winata", 3.5, 2018),
            Data2("Eras Timoty", 3.7, 2018),
            Data2("Jonathan", 3.8, 2018),
            Data2("Yosia", 3.8, 2018),
            Data2("Yotam", 4.0, 2019)
        )
    }
}