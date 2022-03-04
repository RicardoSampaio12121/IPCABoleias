package com.example.ipcaboleias

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


class RidesActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rides)

        var dLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        var navView = findViewById<NavigationView>(R.id.navView)

        toggle = ActionBarDrawerToggle(this, dLayout, R.string.open, R.string.close)
        dLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.miItem1 -> Toast.makeText(applicationContext, "Clicked Item 1", Toast.LENGTH_LONG).show()
                R.id.miItem2 -> Toast.makeText(applicationContext, "Clicked Item 2", Toast.LENGTH_LONG).show()
                R.id.miItem3 -> Toast.makeText(applicationContext, "Clicked Item 3", Toast.LENGTH_LONG).show()
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}