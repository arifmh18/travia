package com.travia

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.travia.databinding.FragmentPesanBinding

class PesanFragment : Fragment() {

    private lateinit var binding: FragmentPesanBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        binding = FragmentPesanBinding.inflate(layoutInflater, container, false)

        binding.apply {
            pesan.setOnClickListener {

                //set email apps chooser
                val intentSelector = Intent(Intent.ACTION_SENDTO)
                intentSelector.data = Uri.parse("mailto:")

                try {

                    //set intent extra for sending to email apps
                    val mailClient = Intent(Intent.ACTION_SEND)
                    mailClient.putExtra(Intent.EXTRA_EMAIL, arrayOf("travinatravelindonesia@gmail.com"))
                    mailClient.putExtra(Intent.EXTRA_SUBJECT, "Email Konfirmasi aplikasi Travina")
                    mailClient.selector = intentSelector // set intent extra chooser
                    startActivity(Intent.createChooser(mailClient, "Pilih aplikasi email anda"))

                } catch (ex: Exception) {
                    Toast.makeText(requireContext(), "${ex.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }
}
