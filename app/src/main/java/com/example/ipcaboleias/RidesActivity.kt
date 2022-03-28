package com.example.ipcaboleias

import android.media.Image
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.replace
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.graphics.toColor
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView


class RidesActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rides)

        val RIDES_FRAG_TAG = "ridesFragTag"
        val FILTER_FRAG_TAG = "filterFragTag"
        val CREATE_PUB_FRAG_TAG = "createPubFragTag"
        val RIDES_DETAILS_FRAG_TAG = "detailsFragTag"

        val dLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navView = findViewById<NavigationView>(R.id.navView)
        val menu = findViewById<ImageButton>(R.id.imageMenu)
        val returnBtn = findViewById<ImageButton>(R.id.imageReturn)
        val filter = findViewById<FrameLayout>(R.id.frameLayout4)
        val frameLayoutFilter = findViewById<FrameLayout>(R.id.frameLayoutFilter)

        //Call rides fragment
        supportFragmentManager.beginTransaction().add(R.id.frameFragment, RidesFragment.newInstance(), RIDES_FRAG_TAG).commit()

        menu.setOnClickListener{

            if(!dLayout.isDrawerOpen(Gravity.LEFT)){
                dLayout.openDrawer(Gravity.LEFT)
            }
            else{
                dLayout.closeDrawer(Gravity.LEFT)
            }
        }

        filter.setOnClickListener{
            var filterFrag = supportFragmentManager.findFragmentByTag(FILTER_FRAG_TAG)

            if(filterFrag == null)
                supportFragmentManager.beginTransaction().add(R.id.frameLayoutFilter, FilterResults.newInstance(), FILTER_FRAG_TAG).commit()
            else{
                supportFragmentManager.beginTransaction().show(filterFrag).commit()
            }
        }

        navView.setNavigationItemSelectedListener {
            if(it.itemId == R.id.miBoleias){
            }
            else if(it.itemId == R.id.miBoleiasAgendadas){
            }
            dLayout.closeDrawer(Gravity.LEFT)
            true
        }

        returnBtn.setOnClickListener{
            val fragToRemove = supportFragmentManager.findFragmentByTag(RIDES_DETAILS_FRAG_TAG)
            val fragToShow = supportFragmentManager.findFragmentByTag(RIDES_FRAG_TAG)

            supportFragmentManager.beginTransaction().remove(fragToRemove!!).commit()
            supportFragmentManager.beginTransaction().show(fragToShow!!).commit()

            returnBtn.visibility = View.GONE
            menu.visibility = View.VISIBLE
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){


            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun changeFragments(){

    }




}