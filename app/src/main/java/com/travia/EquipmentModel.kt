package com.travia

data class EquipmentModel (
    var uuid: String? = null,
    var nama: String? = null,
    var deskripsi: String? = null,
    var stok: String? = null,
    var harga: String? = null,
    var kategori: String? = null,
    var syarat: List<RequirementModel>? = null
)