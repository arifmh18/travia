package com.travia

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.travia.adapter.ViewPagerAdapter
import com.travia.databinding.ActivityMainBinding
import com.travia.ui.fragment.HomeFragment
import com.travia.ui.fragment.KeranjangFragment
import com.travia.ui.fragment.ProfilFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

//
//    val menuText = arrayOf("Beranda", "Keranjang", "Pesan", "Profil")
//    val menuIcon = arrayOf(
//        R.drawable.ic_home,
//        R.drawable.ic_supermarket,
//        R.drawable.ic_mail,
//        R.drawable.ic_user
//    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            startActivity(Intent(this, ListPembayaran::class.java))
        }
//        val adapter = ViewPagerAdapter(this)
//        view_pager.adapter = adapter
//        TabLayoutMediator(
//            tabLayout,
//            view_pager
//        ) { tab, position ->
//            tab.text = menuText[position]
//            tab.icon = ResourcesCompat.getDrawable(resources, menuIcon[position], null)
//        }.attach()
    }

    private fun init() {
        binding.apply {
            bottomNavMain.setOnNavigationItemSelectedListener(this@MainActivity)
            bottomNavMain.selectedItemId = R.id.menuHome
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null

        when(item.itemId){
            R.id.menuHome -> {
                fragment = HomeFragment()
            }
            R.id.menuCart -> {
                fragment = KeranjangFragment()
            }
            R.id.menuMessage -> {
                fragment = PesanFragment()
            }
            R.id.menuProfile -> {
                fragment = ProfilFragment()
            }
        }

        return loadFragment(fragment = fragment)
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null){
            supportFragmentManager.apply {
                beginTransaction()
                    .replace(R.id.frameLayoutMain, fragment)
                    .commit()
                return true
            }
        }
        return false
    }
}