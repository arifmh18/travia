package com.travia

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wisata")
data class WisataModel(
    @PrimaryKey val uuid: String,
    val nama: String,
    val deskripsi: String,
    val kategory: String,
    val harga: String,
    val video_link: String,
    val location: LocationModel,
    var jadwal: ScheduleModel? = null,
    val rekomendasi: Boolean = false
){
    constructor(): this("", "", "", "", "", "", LocationModel("", "", ""), null)
}