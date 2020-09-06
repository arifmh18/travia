package com.travia.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.travia.MainActivity
import com.travia.databinding.ActivityCariBinding

class CariActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCariBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCariBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.apply {
            tbCari.setOnClickListener {
                startActivity(Intent(this@CariActivity, MainActivity::class.java))
                finish()
            }
        }
    }
}