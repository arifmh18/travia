package com.travia.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.travia.LoginActivity
import com.travia.R
import kotlinx.android.synthetic.main.fragment_profil.*
import com.travia.databinding.FragmentProfilBinding
import com.travia.ui.mitra.add_destination.AddDestinationActivity
import com.travia.ui.mitra.add_equipment.AddEquipmentActivity
import kotlinx.android.synthetic.main.fragment_profil.*

class ProfilFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentProfilBinding

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
    }

    companion object {
    }
}