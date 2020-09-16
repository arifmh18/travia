package com.travia.ui.wisatawan.list_destination

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.travia.WisataModel
import com.travia.database.entity.TransaksiEntity
import com.travia.databinding.ActivityDestinationListBinding
import com.travia.utils.Auth
import com.travia.utils.hide
import com.travia.viewModel.TransaksiViewModel
import kotlinx.android.synthetic.main.fragment_pesan.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class DestinationListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDestinationListBinding

    private lateinit var database: FirebaseDatabase

    private lateinit var adapter: DestinationAdapter

    private var destinationType = 1

    private val viewModel by viewModels<TransaksiViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDestinationListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        database = FirebaseDatabase.getInstance()
        viewModel.init(this)

        val intent = intent.extras
        if (intent != null) {
            destinationType = intent.getInt(DESTINATION_TYPE)
            binding.tbDestinationList.title = intent.getString(TOOLBAR_TITLE)
        }

        adapter = DestinationAdapter(this) { model ->
            viewModel.allTransaksi.observe(this, Observer { list ->
                val key: String = "kd" + System.currentTimeMillis()
                val kd: String = "Tran" + Random.nextInt(0, 9999) + "Kd" + Random.nextInt(0, 9999)
                if (list.count() == 0) {
                    val jumlah = 1
                    val data = TransaksiEntity(
                        key,
                        kd,
                        Auth().currentNow().uid,
                        model.nama,
                        jumlah,
                        jumlah * model.harga.toInt(),
                        false,
                        Date().toString(),
                        model.gambar!![0]
                    )
                    viewModel.insert(data)
                }
            })
        }

        binding.apply {
            tbDestinationList.setNavigationOnClickListener {
                finish()
            }

            rvDestinationList.layoutManager = GridLayoutManager(this@DestinationListActivity, 2)
            rvDestinationList.adapter = adapter
        }

        getDestinationList()
    }

    private fun getDestinationList() {

        if (destinationType == CITY_TYPE){
            database.reference.child("wisata").orderByChild("kategory").equalTo("Kota")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val destinationList: ArrayList<WisataModel> = ArrayList()
                        for (data in snapshot.children) {
                            val wisata = data.getValue(WisataModel::class.java)
                            destinationList.add(wisata!!)
                        }

                        adapter.setDestinations(destinationList = destinationList)
                        binding.pbLoadDestinationList.hide()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@DestinationListActivity, error.message, Toast.LENGTH_SHORT).show()
                        binding.pbLoadDestinationList.hide()
                    }
                })
        }else if(destinationType == NATURE_TYPE){
            database.reference.child("wisata").orderByChild("kategory").equalTo("alam")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val destinationList: ArrayList<WisataModel> = ArrayList()
                        for (data in snapshot.children) {
                            val wisata = data.getValue(WisataModel::class.java)
                            destinationList.add(wisata!!)
                        }

                        adapter.setDestinations(destinationList = destinationList)
                        binding.pbLoadDestinationList.hide()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@DestinationListActivity, error.message, Toast.LENGTH_SHORT).show()
                        binding.pbLoadDestinationList.hide()
                    }
                })
        }
    }

    companion object {
        val DESTINATION_TYPE = "DESTINATION_TYPE"
        val TOOLBAR_TITLE = "TOOLBAR_TITLE"
        var CITY_TYPE = 1
        var NATURE_TYPE = 2
        var TAG = DestinationListActivity::class.java.simpleName
    }

}