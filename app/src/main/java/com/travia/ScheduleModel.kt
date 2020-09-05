package com.travia

data class ScheduleModel (
    var senin: DayScheduleModel? = null,
    var selasa: DayScheduleModel? = null,
    var rabu: DayScheduleModel? = null,
    var kamis: DayScheduleModel? = null,
    var jumat: DayScheduleModel? = null,
    var sabtu: DayScheduleModel? = null,
    var minggu: DayScheduleModel? = null,
)