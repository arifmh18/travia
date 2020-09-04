package com.travia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.travia.databinding.ActivityRegisterMitraBinding
import com.travia.model.Users
import com.travia.utils.showToast

class RegisterMitra : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterMitraBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var ref: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterMitraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance()

        setSpinner()
    }

    private fun setSpinner() {
        val adapter =
            ArrayAdapter.createFromResource(baseContext, R.array.jk_string, R.layout.list_item)
        val adapRole =
            ArrayAdapter.createFromResource(baseContext, R.array.role, R.layout.list_item)
        binding.apply {
            jk.adapter = adapter
            nama.setText(auth.currentUser!!.displayName)
            SpinRole.adapter = adapRole
            btnSimpan.setOnClickListener {
                validasiInput()
            }
        }
    }

    private fun validasiInput() {
        binding.apply {
            when {
                nama.text.isNullOrBlank() -> {
                    showToast(this@RegisterMitra, "Isikan Nama Anda!!")
                }
                jk.selectedItemPosition == 0 -> {
                    showToast(this@RegisterMitra, "Pilih Jenis Kelamin Anda!!")
                }
                notelp.text.isNullOrBlank() -> {
                    showToast(this@RegisterMitra, "Isikan No telp Anda!!")
                }
                SpinRole.selectedItemPosition == 0 -> {
                    showToast(this@RegisterMitra, "Pilih Jenis User Anda!!")
                }
                else -> {
                    simpan(
                        nama.text.toString(),
                        notelp.text.toString(),
                        jk.selectedItem.toString(),
                        SpinRole.selectedItem.toString()
                    )
                }
            }
        }
    }

    private fun simpan(nm: String, telp: String, jk: String, rl: String) {
        var curr = auth.currentUser
        var data = Users(curr!!.uid, nm, curr.email.toString(), telp, jk, rl, "Google", "Google")
        ref.getReference("users").child(auth.currentUser!!.uid).setValue(data)
            .addOnCompleteListener { rest ->
                if (rest.isSuccessful) {
                    startActivity(Intent(this@RegisterMitra, MainActivity::class.java))
                    finish()
                }
            }
            .addOnFailureListener { err ->
                showToast(this@RegisterMitra, "error : " + err.message)
            }
    }
}