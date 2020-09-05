package com.travia.utils

fun setLeadingZero(time: Int): String{
    return if (time >= 10){
        time.toString()
    }else {
        "0$time"
    }
}