package com.travia.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.travia.LoginActivity
import com.travia.R
import com.travia.databinding.FragmentProfilBinding
import com.travia.model.Users
import com.travia.ui.mitra.add_destination.AddDestinationActivity
import com.travia.ui.mitra.add_equipment.AddEquipmentActivity
import com.travia.utils.hide
import com.travia.utils.show

class ProfilFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentProfilBinding
    private lateinit var ref: FirebaseDatabase

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
                requireActivity().finish()
            }
            btnEditWisata.setOnClickListener {
                startActivity(Intent(requireContext(), AddDestinationActivity::class.java))
            }

            btnTambahPeralatan.setOnClickListener {
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

                                    //Check user role for hide / showing ui by their roles
                                    when (data.role) {
                                        "Wisatawan" -> {
                                            btnEditWisata.hide()
                                            btnTambahPeralatan.hide()
                                            hideShimmer()
                                        }
                                        "Mitra Usaha" -> {
                                            checkIfDestinationExist() // Check if mitra already add wisata
                                        }
                                        "Pemandu Wisata" -> {
                                            btnEditWisata.hide()
                                            btnTambahPeralatan.hide()
                                            hideShimmer()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT)
                        .show()
                    Log.e(TAG, "onCancelled: ${error.message}",)
                    hideShimmer()
                }
            })
    }

    //Check if user already add destination
    private fun checkIfDestinationExist() {
        ref.reference.child("wisata")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        if (data.key == auth.currentUser?.uid) {
                            binding.apply {
                                tvAddDestinationProfile.text = getString(R.string.edit_destination)
                            }
                        } else {
                            binding.apply {
                                tvAddDestinationProfile.text = getString(R.string.add_destination)
                            }
                        }
                        hideShimmer()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "onCancelled: ${error.message}")
                    hideShimmer()
                }
            })
    }

    private fun hideShimmer(){
        binding.apply {
            containerProfileShimmer.stopShimmer()
            containerProfileShimmer.hide()
            containerProfile.show()
        }
    }

    companion object {
        var TAG = ProfilFragment::class.java.simpleName
    }
}