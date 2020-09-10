package com.travia.model

import com.travia.LocationModel

data class Culinary(
    val nama: String,
    val deskripsi: String,
    val harga: String,
    val gambar: String,
    val lokasi: LocationModel,
    val kategori: String,
    val rekomendasi: Boolean
){
    constructor(): this("", "", "", "", LocationModel("", "", ""), "", false)
}