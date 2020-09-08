package com.travia.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import com.travia.adapter.WisataAdapter
import com.travia.database.entity.WisataEntity
import com.travia.databinding.ActivityCariBinding
import com.travia.viewModel.WisataViewModel
import java.util.*
import kotlin.collections.ArrayList
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import com.travia.ui.wisatawan.list_destination.DestinationListActivity

class CariActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCariBinding
    private val viewModel by viewModels<WisataViewModel>()
    private var wisatas: ArrayList<WisataEntity> = ArrayList()
    private lateinit var adapter: WisataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCariBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.init(this@CariActivity)
        init()
    }

    private fun init() {
        adapter = WisataAdapter(this@CariActivity)
        val intent = intent.extras
        binding.apply {
            tbCari.title = "Search Wisata"
            tbCari.setNavigationOnClickListener {
                finish()
            }
            recylerviewCari.layoutManager = GridLayoutManager(this@CariActivity, 2)
            recylerviewCari.adapter = adapter
            tabKategori.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    edtSearch.addTextChangedListener {
                         cekPosition(tab!!.position, edtSearch.text.toString())
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    return
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    return cekPosition(tab!!.position, edtSearch.text.toString())
                }
            })
        }

    }

    private fun cekPosition(position: Int, nama: String) {
        val query:String = "%$nama%"
        if (position == 0) {
            getSearch("alam", query)
        }else{
            getSearch("Kota", query)
        }
    }

    private fun getSearch(ctgr: String, query: String) {
        viewModel.search(query, ctgr).observe(this, object :Observer<List<WisataEntity>>{
            override fun onChanged(t: List<WisataEntity>?) {
                if(t != null){
                    adapter.setDestinations(t)
                }else{
                    return
                }
            }
        })
    }
}