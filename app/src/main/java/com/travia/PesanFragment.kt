package com.travia

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class PesanFragment : Fragment() {

    lateinit var fragview:View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val pesan = fragview.findViewById<Button>(R.id.pesan) as Button
        pesan.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("gigihrozak355@gmail.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Email dari Aplikasi Android")
            try {
                startActivity(Intent.createChooser(intent, "Ingin Mengirim Email ?"))
            } catch (ex: ActivityNotFoundException) {
                //do something else
            }
        }
        return fragview
    }
}
