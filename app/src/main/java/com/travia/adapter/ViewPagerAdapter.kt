package com.travia.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.travia.ui.fragment.BerandaFragment
import com.travia.ui.fragment.KeranjangFragment
import com.travia.ui.fragment.PesanFragment
import com.travia.ui.fragment.ProfilFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val JML = 4

    override fun getItemCount(): Int {
        return JML
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                BerandaFragment()
            }
            1 -> {
                KeranjangFragment()
            }
            2 -> {
                PesanFragment()
            }
            3 -> {
                ProfilFragment()
            }
            else -> {
                BerandaFragment()
            }
        }
    }

}