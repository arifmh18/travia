package com.travia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.travia.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val menuText = arrayOf("Beranda", "Keranjang", "Pesan", "Profil")
    val menuIcon = arrayOf(
        R.drawable.ic_home,
        R.drawable.ic_supermarket,
        R.drawable.ic_mail,
        R.drawable.ic_user
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = ViewPagerAdapter(this)
        view_pager.adapter = adapter
        TabLayoutMediator(
            tabLayout,
            view_pager
        ) { tab, position ->
            tab.text = menuText[position]
            tab.icon = ResourcesCompat.getDrawable(resources, menuIcon[position], null)
        }.attach()
    }
}