package com.example.ugd_3_kelompok.api

class PemesananKamarApi {
    companion object {
        val BASE_URL = "http://192.168.1.2/ci4-apiserver/public/"

        val GET_ALL_URL = BASE_URL + "pemesananKamar/"
        val GET_BY_ID_URL = BASE_URL + "pemesananKamar/"
        val ADD_URL = BASE_URL + "pemesananKamar/"
        val UPDATE_URL = BASE_URL + "pemesananKamar/"
        val DELETE_URL = BASE_URL + "pemesananKamar/"
    }
}