package com.travia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.travia.databinding.ActivityRegisterUserBinding

class RegisterUser : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.apply {
            masuk.setOnClickListener {
                startActivity(Intent(this@RegisterUser, LoginActivity::class.java))
            }
        }
    }
}