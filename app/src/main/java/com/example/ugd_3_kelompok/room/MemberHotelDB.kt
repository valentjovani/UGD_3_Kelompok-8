package com.example.ugd_3_kelompok.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [MemberHotel::class],
    version = 1
)

abstract class MemberHotelDB: RoomDatabase() {
    abstract fun MemberHotelDao() : MemberHotelDao
    companion object {
        @Volatile private var instance : MemberHotelDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?:
        synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }
        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            MemberHotelDB::class.java,
            "memberhotel12345.db"
        ).build()
    }
}