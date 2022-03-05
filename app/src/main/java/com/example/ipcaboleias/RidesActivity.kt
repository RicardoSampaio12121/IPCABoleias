package com.example.ipcaboleias

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView


class RidesActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rides)

        var rv = findViewById<RecyclerView>(R.id.rvPublications)


        var publicationList = mutableListOf(
            RVPublication("Ricardo", "Braga", "Barcelos", "24/08/2022", 4.0F),
            RVPublication("José", "Porto", "Braga", "27/08/2022", 9.0F),
            RVPublication("Sampaio", "Esposende", "Guimaraes", "29/08/2022", 12.4F),
            RVPublication("Ricardo", "Braga", "Barcelos", "24/08/2022", 4.0F),
            RVPublication("José", "Porto", "Braga", "27/08/2022", 9.0F),
            RVPublication("Sampaio", "Esposende", "Guimaraes", "29/08/2022", 12.4F),
            RVPublication("Ricardo", "Braga", "Barcelos", "24/08/2022", 4.0F),
            RVPublication("José", "Porto", "Braga", "27/08/2022", 9.0F),
            RVPublication("Sampaio", "Esposende", "Guimaraes", "29/08/2022", 12.4F),
            RVPublication("Ricardo", "Braga", "Barcelos", "24/08/2022", 4.0F),
            RVPublication("José", "Porto", "Braga", "27/08/2022", 9.0F),
            RVPublication("Sampaio", "Esposende", "Guimaraes", "29/08/2022", 12.4F),
            RVPublication("Ricardo", "Braga", "Barcelos", "24/08/2022", 4.0F),
            RVPublication("José", "Porto", "Braga", "27/08/2022", 9.0F),
            RVPublication("Sampaio", "Esposende", "Guimaraes", "29/08/2022", 12.4F)
        )

        val adapter = RVPublicationsAadapter(publicationList)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)
        adapter.setOnItemClickListener(object : RVPublicationsAadapter.onItemClickListener{
            override fun onItemClick(position: Int){
                Toast.makeText(applicationContext, "Item clicado: $position", Toast.LENGTH_LONG).show()
            }
        })


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