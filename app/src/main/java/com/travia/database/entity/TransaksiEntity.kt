package com.travia.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "transaksi")
data class TransaksiEntity(
    @PrimaryKey val kd_tranksasi: String,
    val uid: String,
    val jumlah: String,
    val total: String,
    val bayar: Boolean = false,
    val tanggal: String,
) {
    constructor() : this("", "", "","", false, "")
}