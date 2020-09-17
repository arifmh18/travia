package com.travia.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Auth() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun currentNow(): FirebaseUser {
        return auth.currentUser!!
    }
}