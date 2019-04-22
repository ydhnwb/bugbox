package com.ydhnwb.harambe

import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.fragment.app.Fragment
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import com.ydhnwb.harambe.fragments.FragmentBug
import com.ydhnwb.harambe.fragments.FragmentSearch
import com.ydhnwb.harambe.fragments.FragmentShow

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        private var openFirst = true
        private var navStatus = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initComp()
        if(savedInstanceState == null){
            openFirst = true
            val item = nav_view.getMenu().getItem(0).setChecked(true)
            onNavigationItemSelected(item)
        }
    }

    override fun onBackPressed() { if (drawer_layout.isDrawerOpen(GravityCompat.START)) { drawer_layout.closeDrawer(GravityCompat.START) } else { super.onBackPressed() } }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment : Fragment? = null
        when (item.itemId) {
            R.id.nav_new -> {
                if(navStatus == 0 && !openFirst){
                    drawer_layout.closeDrawer(GravityCompat.START)
                }else{
                    navStatus = 0
                    openFirst = false
                    fragment = FragmentBug()
                }
            }
            R.id.nav_show -> {
                if(navStatus == 1 && !openFirst){
                    drawer_layout.closeDrawer(GravityCompat.START)
                }else{
                    openFirst = false
                    navStatus = 1
                    fragment = FragmentShow()
                }
            }
            R.id.nav_search -> {
                if(navStatus == 2 && !openFirst){
                    drawer_layout.closeDrawer(GravityCompat.START)
                }else{
                    openFirst = false
                    navStatus = 2
                    fragment = FragmentSearch()
                }
            }

            else -> {
                openFirst = false
                navStatus = 0
                fragment = FragmentBug()
            }
        }

        if(fragment != null){
            val fm = supportFragmentManager
            val ft = fm.beginTransaction()
            ft.replace(R.id.container_fragment, fragment)
            ft.commit()
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initComp(){
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }
}
