package com.travia.ui.pemandu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.travia.databinding.ActivityDetailListBinding

class DetailListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
    }

    private fun initData() {
        val i = intent.extras
        binding.apply {

        }
    }
}