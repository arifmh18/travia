package com.travia.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.travia.R
import com.travia.adapter.TransaksiAdapter
import com.travia.database.entity.TransaksiEntity
import com.travia.databinding.FragmentKeranjangBinding
import com.travia.viewModel.TransaksiViewModel

class KeranjangFragment : Fragment() {

    private lateinit var binding: FragmentKeranjangBinding
    private val viewModel by viewModels<TransaksiViewModel>()
    private lateinit var listData: List<TransaksiEntity>

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
        initSync()
    }

    private fun initSync() {
        val adapter = TransaksiAdapter(requireContext()) {

        }
        binding.apply {
            listKeranjang.adapter = adapter
        }
        viewModel.allTransaksi.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })

    }
}