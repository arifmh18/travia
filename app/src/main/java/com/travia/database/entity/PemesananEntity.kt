package com.travia.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pesanan")
data class PemesananEntity(
    @PrimaryKey var kd_pesanan: String,
    val kd_transaksi: String,
    val kd_sewa: String,
    val jenis: String,
    val jumlah: Int,
    val total: Int
) {
}