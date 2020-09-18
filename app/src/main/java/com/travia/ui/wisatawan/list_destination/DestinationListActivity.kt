package com.travia.ui.wisatawan.list_destination

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.travia.R
import com.travia.WisataModel
import com.travia.database.entity.TransaksiEntity
import com.travia.databinding.ActivityDestinationListBinding
import com.travia.ui.fragment.KeranjangFragment
import com.travia.utils.Auth
import com.travia.utils.hide
import com.travia.utils.showToast
import com.travia.viewModel.TransaksiViewModel
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
                val kd: String = "Tran ${Random.nextInt(0, 9999)} kd ${Random.nextInt(0, 9999)}"
                if (list.count() == 0) {
                    val data = TransaksiEntity(
                        model.uuid,
                        kd,
                        Auth().currentNow().uid,
                        model.nama,
                        1,
                        1 * model.harga.toInt(),
                        model.harga,
                        false,
                        Date().toString(),
                        "Tidak"
                    )
                    viewModel.insert(data)
                } else {
                    var update = false
                    var trans: String = ""
                    for (h in list) {
                        trans = h.kd_tranksasi
                        if (h.key == model.uuid) {
//                             val data = TransaksiEntity(
//                                h.key,
//                                h.kd_tranksasi,
//                                Auth().currentNow().uid,
//                                model.nama,
//                                h.jumlah + 1,
//                                (h.jumlah +1) * model.harga.toInt(),
//                                model.harga,
//                                false,
//                                Date().toString(),
//                                "Tidak"
//                            )
//                            viewModel.update(data)
//                            Log.d("Sudah : ", h.jumlah.toString())
//                            startActivity(Intent(this, KeranjangFragment::class.java))
//                            finish()
                            showToast(this, "Wisata Sudah Ada diKeranjang")
                            update = true
                        }
                    }
                    if (!update) {

                        val data = TransaksiEntity(
                            model.uuid,
                            trans,
                            Auth().currentNow().uid,
                            model.nama,
                            1,
                            (1 * model.harga.toInt()),
                            model.harga,
                            false,
                            Date().toString(),
                            "Tidak"
                        )
                        viewModel.insert(data)
                    }
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
                        Toast.makeText(
                            this@DestinationListActivity,
                            error.message,
                            Toast.LENGTH_SHORT
                        ).show()
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
                        Toast.makeText(
                            this@DestinationListActivity,
                            error.message,
                            Toast.LENGTH_SHORT
                        ).show()
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