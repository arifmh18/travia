package com.travia.model

data class PemanduModel(
    val uid: String,
    val nama: String,
    val sertifikasi: String,
    val moto: String,
    val harga: String,
    val waktu: String,
    val id_wisata: String,
    val status: String,
    val foto: String
) {
    constructor() : this("", "", "", "", "", "", "", "", "")
}