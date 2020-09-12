package com.travia

data class WisataModel(
    val uuid: String,
    val nama: String,
    val deskripsi: String,
    val kategory: String,
    var gambar: List<String>? = null,
    val harga: String,
    val video_link: String,
    val location: LocationModel,
    var jadwal: ScheduleModel? = null,
    var requirement: List<RequirementModel>? = null,
    val rekomendasi: Boolean = false
){
    constructor(): this("", "", "", "", emptyList<String>(), "", "", LocationModel("", "", ""), null, emptyList(),
        false)
}