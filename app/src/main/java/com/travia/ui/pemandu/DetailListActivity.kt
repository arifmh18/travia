package com.travia.ui.pemandu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.travia.databinding.ActivityDetailListBinding
import com.travia.model.PemanduModel
import com.travia.model.Users
import com.travia.utils.showToast

class DetailListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailListBinding
    private lateinit var ref: FirebaseDatabase
    private lateinit var user: Users
    private lateinit var pemandu: PemanduModel
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ref = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        val i = intent.extras!!.getString("uid")
        getPemandu(i)
    }

    private fun getPemandu(uid: String?) {
        ref.getReference("pemandu/").child(uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        pemandu = snapshot.getValue(PemanduModel::class.java)!!
                        getUser(uid)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showToast(this@DetailListActivity, error.message)
                }

            })
    }

    private fun getUser(uid: String?) {
        ref.getReference("users/").child(uid!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    user = snapshot.getValue(Users::class.java)!!
                    initData()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast(baseContext, error.message)
            }

        })
    }

    private fun initData() {
        binding.apply {
            if (user.foto != "Tidak") {
                Glide.with(baseContext).load(user.foto).into(imgDePemandu)
            } else if (user.foto == "Google") {
                Glide.with(baseContext).load(auth.currentUser!!.photoUrl).into(imgDePemandu)
            }
            txtDeHarga.text = "Rp. ${pemandu.harga} / ${pemandu.waktu} Jam "
            txtDeNama.text = user.nama
            txtDeMoto.text = pemandu.moto
            txtDephone.text = user.no_telp
            btnBack.setOnClickListener {
                finish()
            }
        }
    }
}