package com.travia.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wisata")
data class WisataEntity(
    @PrimaryKey val uuid: String,
    val nama: String,
    val deskripsi: String,
    val kategory: String,
    val harga: String,
    val video_link: String,
    val kota: String,
    val rekomendasi: Boolean = false
) {
    constructor() : this("","","","","","","", false)
}