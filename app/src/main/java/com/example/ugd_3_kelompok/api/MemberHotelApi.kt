package com.example.ugd_3_kelompok.api

class MemberHotelApi {
    companion object {
        val BASE_URL = "http://192.168.1.2/ci4-apiserver/public/"

        val GET_ALL_URL = BASE_URL + "memberHotel/"
        val GET_BY_ID_URL = BASE_URL + "memberHotel/"
        val ADD_URL = BASE_URL + "memberHotel/"
        val UPDATE_URL = BASE_URL + "memberHotel/"
        val DELETE_URL = BASE_URL + "memberHotel/"
    }
}