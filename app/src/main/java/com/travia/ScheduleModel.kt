package com.travia

data class ScheduleModel (
    val senin: DayScheduleModel,
    val selasa: DayScheduleModel,
    val rabu: DayScheduleModel,
    val kamis: DayScheduleModel,
    val jumat: DayScheduleModel,
    val sabtu: DayScheduleModel,
    val minggu: DayScheduleModel,
)