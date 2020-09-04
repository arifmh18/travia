package com.travia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.travia.databinding.ActivityRegisterUserBinding
import kotlinx.android.synthetic.main.activity_login.*

class Register_User : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.apply {
            daftarMitr.setOnClickListener {
                startActivity(Intent(this@Register_User, Register_Mitra::class.java))
            }
        }
    }
}