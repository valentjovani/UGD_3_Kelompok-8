package com.example.ugd_3_kelompok.room

import androidx.room.*

@Dao
interface MemberHotelDao {
    @Insert
    fun addMemberHotel(note: MemberHotel)

    @Update
    fun updateMemberHotel(note: MemberHotel)

    @Delete
    fun deleteMemberHotel(note: MemberHotel)

    @Query("SELECT * FROM MemberHotel")
    suspend fun getMemberHotel() : List<MemberHotel>

    @Query("SELECT * FROM MemberHotel WHERE id =:user_id")
    suspend fun getMemberHotel(user_id: Int) : List<MemberHotel>
}