package com.travia.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaksi")
data class TransaksiEntity(
    @PrimaryKey val key: String,
    val kd_tranksasi: String,
    val uid: String,
    val nama: String,
    val jumlah: Int,
    val total: Int,
    val bayar: Boolean = false,
    val tanggal: String,
    val foto: String
) {
    constructor() : this("", "", "", "", 0, 0, false, "", "")
}