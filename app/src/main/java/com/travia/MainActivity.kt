package com.travia

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

    private lateinit var sharedPrefHelper: SharedPrefHelper

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
    }

    private fun init() {

        sharedPrefHelper = SharedPrefHelper(context =  this)

        val user_role = sharedPrefHelper.getPrefencesRole()
        Log.d(TAG, "init: $user_role")

        binding.apply {
            bottomNavMain.setOnNavigationItemSelectedListener(this@MainActivity)
            bottomNavMain.selectedItemId = R.id.menuHome

            if (user_role == "Mitra Usaha" || user_role == "Pemandu Wisata"){
//                bottomNavMain.menu.removeItem(R.id.menuCart)
//                bottomNavMain.menu.removeItem(R.id.menuMessage)
                bottomNavMain.menu.findItem(R.id.menuCart).isVisible = false
                bottomNavMain.menu.findItem(R.id.menuMessage).isVisible = false
                fab.hide()
            }
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

    companion object {
        var TAG = MainActivity::class.java.simpleName
    }
}