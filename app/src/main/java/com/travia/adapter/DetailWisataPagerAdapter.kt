package com.travia.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.travia.RequirementModel
import com.travia.ui.wisatawan.list_destination.detail_wisata.DeskripsiWisataFragment
import com.travia.ui.wisatawan.list_destination.detail_wisata.SyaratWisataFragment

class DetailWisataPagerAdapter(fragmentActivity: FragmentActivity, private var wisataModel: String): FragmentStateAdapter(fragmentActivity) {

    private val tabCount = 2

    override fun getItemCount() = tabCount

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                SyaratWisataFragment.newInstance(wisataModel = wisataModel)
            }
            1 -> {
                DeskripsiWisataFragment.newInstance(wisataModel = wisataModel)
            }
            else -> {
                SyaratWisataFragment.newInstance(wisataModel = wisataModel)
            }
        }
    }

}