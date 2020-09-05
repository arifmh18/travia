package com.travia.model

class Users(
    var uid: String,
    var nama: String,
    var email: String,
    var no_telp: String,
    var JK: String,
    var role: String,
    var password: String,
    var foto: String
) {
    constructor() : this("", "", "", "", "", "", "", "")
}