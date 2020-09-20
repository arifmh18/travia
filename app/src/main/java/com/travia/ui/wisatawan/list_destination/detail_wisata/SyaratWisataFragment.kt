package com.travia.ui.wisatawan.list_destination.detail_wisata

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.travia.R
import com.travia.RequirementModel
import com.travia.WisataModel
import com.travia.databinding.FragmentSyaratWisataBinding
import com.travia.utils.hide
import com.travia.utils.show

class SyaratWisataFragment : Fragment() {

    private lateinit var binding: FragmentSyaratWisataBinding

    private lateinit var adapterRequirement: RequirementAdapter

    private lateinit var wisataModel: WisataModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSyaratWisataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setData()
    }

    private fun init() {
        arguments?.apply {
            getString(TAG_WISATA_MODEL)?.let {
                wisataModel = getWisataModel(wisataModelString = it)
            }
        }

        adapterRequirement = RequirementAdapter(context = requireContext())

        binding.apply {
            rvEquipmentDetailDestination.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = adapterRequirement
            }
        }
    }

    private fun setData() {

        binding.apply {
            if (wisataModel.requirement!!.isNotEmpty()){
                adapterRequirement.setRequirements(wisataModel.requirement!!)
                pbEquipmentDetailDestination.hide()
            }else {
                tvNoRequirementDetailDestination.show()
                pbEquipmentDetailDestination.hide()
            }
        }
    }

    private fun getWisataModel(wisataModelString: String): WisataModel {
        return Gson().fromJson(wisataModelString, WisataModel::class.java)
    }

    companion object {
        var TAG = SyaratWisataFragment::class.java.simpleName

        var TAG_WISATA_MODEL = "wisataModel"

        fun newInstance(wisataModel: String): SyaratWisataFragment {
            val bundle = Bundle().apply {
                putString(TAG_WISATA_MODEL, wisataModel)
            }

            return SyaratWisataFragment().apply {
                arguments = bundle
            }
        }
    }
}