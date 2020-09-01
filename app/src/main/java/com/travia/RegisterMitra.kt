package com.travia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContentProviderCompat.requireContext
import com.travia.databinding.ActivityRegisterMitraBinding

class RegisterMitra : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterMitraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterMitraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSpinner()
    }

    private fun setSpinner() {
        binding.apply {
            val items = listOf("Material", "Design", "Components", "Android")
            val adapter = ArrayAdapter(baseContext, R.layout.list_item_jk, items)
            (jk.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        }
    }
}