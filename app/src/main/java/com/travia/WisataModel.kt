package com.travia

data class WisataModel(
    val uuid: String,
    val nama: String,
    val deskripsi: String,
    val kategory: String,
    val harga: String,
    val video_link: String,
    val lokasi: LokasiModel
)