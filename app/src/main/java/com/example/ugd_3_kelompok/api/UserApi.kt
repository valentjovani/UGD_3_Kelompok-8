package com.example.ugd_3_kelompok.api

class UserApi {
    companion object {
        val BASE_URL = "http://192.168.1.2/ci4-apiserver/public/"

        val GET_BY_ID_URL = BASE_URL + "user/"
        val ADD_URL = BASE_URL + "user/"
        val UPDATE_URL = BASE_URL + "user/"

        val LOGIN = BASE_URL + "login/"
    }
}