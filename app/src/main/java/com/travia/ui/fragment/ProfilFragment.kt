package com.travia.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.travia.LoginActivity
import com.travia.databinding.FragmentProfilBinding
import com.travia.model.Users
import com.travia.ui.mitra.add_destination.AddDestinationActivity
import com.travia.ui.mitra.add_equipment.AddEquipmentActivity

class ProfilFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentProfilBinding
    private lateinit var ref: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance()
        init()
    }

    private fun init() {
        binding.apply {
            btnLogout.setOnClickListener {
                auth.signOut()
                startActivity(Intent(context, LoginActivity::class.java))
                activity!!.finish()
            }
            btnEditWisata.setOnClickListener {
                startActivity(Intent(requireContext(), AddDestinationActivity::class.java))
            }

            btnPesanan.setOnClickListener {
                startActivity(Intent(requireContext(), AddEquipmentActivity::class.java))
            }
        }
        syncProfile()
    }

    private fun syncProfile() {
        val user = auth.currentUser
        ref.getReference("users/")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (h in snapshot.children) {
                            val data = h.getValue(Users::class.java)
                            if (user!!.uid == data!!.uid) {
                                binding.apply {
                                    if (data.foto == "Google") {
                                        println("paap ${user.photoUrl}")
                                        Glide.with(context!!).load(user.photoUrl)
                                            .into(photoProfile)
                                    }
                                    profileNama.text = data.nama
                                    profileEmail.text = data.email
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    companion object {
    }
}