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
import com.travia.databinding.FragmentProfilBinding
import kotlinx.android.synthetic.main.fragment_profil.*

class ProfilFragment : Fragment() {
    private lateinit var binding: FragmentProfilBinding
    private lateinit var auth: FirebaseAuth

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
            btn_logout.setOnClickListener {
                auth.signOut()
                startActivity(Intent(context, LoginActivity::class.java))
                activity!!.finish()
            }
        }
    }

    companion object {
    }
}