package com.travia.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.travia.adapter.TransaksiAdapter
import com.travia.database.entity.TransaksiEntity
import com.travia.databinding.FragmentKeranjangBinding
import com.travia.utils.Auth
import com.travia.viewModel.TransaksiViewModel
import java.util.*

class KeranjangFragment : Fragment() {

    private lateinit var binding: FragmentKeranjangBinding
    private val viewModel by viewModels<TransaksiViewModel>()

    //    private val models by viewModels<WisataViewModel>()
    private lateinit var listData: List<TransaksiEntity>
    private var totalB: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentKeranjangBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.init(requireContext())
//        models.init(requireContext())
        initSync()
    }

    private fun initSync() {
        val adapter = TransaksiAdapter(requireContext()) { mod, status ->
            if (status == 1) {
                val data = TransaksiEntity(
                    mod.key,
                    mod.kd_tranksasi,
                    Auth().currentNow().uid,
                    mod.nama,
                    mod.jumlah + 1,
                    (mod.jumlah + 1) * mod.harga.toInt(),
                    mod.harga,
                    false,
                    Date().toString(),
                    "Tidak"
                )
                viewModel.update(data)
            } else if (status == 2) {
                val data = TransaksiEntity(
                    mod.key,
                    mod.kd_tranksasi,
                    Auth().currentNow().uid,
                    mod.nama,
                    mod.jumlah - 1,
                    (mod.jumlah - 1) * mod.harga.toInt(),
                    mod.harga,
                    false,
                    Date().toString(),
                    "Tidak"
                )
                viewModel.update(data)
            } else {
                viewModel.delete(mod.key)
            }
        }
        viewModel.allTransaksi.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
            for (h in it) {
                totalB += h.total
            }
            binding.ttlJumlah.text = totalB.toString()

        })

        binding.apply {
            listKeranjang.layoutManager = LinearLayoutManager(context)
            listKeranjang.adapter = adapter
        }

    }
}