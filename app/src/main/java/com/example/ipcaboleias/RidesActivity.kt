package com.example.ipcaboleias

import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ipcaboleias.firebaseRepository.UsersRepository
import com.example.ipcaboleias.rides.FilterResults
import com.example.ipcaboleias.rides.RidesFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.FirebaseDatabase
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*


class RidesActivity : AppCompatActivity() {

    private lateinit var menu: Menu
    lateinit var toggle: ActionBarDrawerToggle

    private val RIDES_FRAG_TAG = "ridesFragTag"
    private val FILTER_FRAG_TAG = "filterFragTag"
    private val CREATE_PUB_FRAG_TAG = "createPubFragTag"
    private val RIDES_DETAILS_FRAG_TAG = "detailsFragTag"
    private val CHAT_CHANNELS_FRAG_TAG = "chatChannelsFragTag"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rides)

        val dLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navView = findViewById<NavigationView>(R.id.navView)
        val menu = findViewById<ImageButton>(R.id.imageMenu)
        val returnBtn = findViewById<ImageButton>(R.id.imageReturn)
        val filter = findViewById<FrameLayout>(R.id.frameLayout4)
        val frameLayoutFilter = findViewById<FrameLayout>(R.id.frameLayoutFilter)

        val usersRepo = UsersRepository(this)

        //Call rides fragment
        supportFragmentManager.beginTransaction()
            .add(R.id.frameFragment, RidesFragment.newInstance(), RIDES_FRAG_TAG).commit()

        // Preencher campos do navigation header com os dados da base de dados

        val header = navView.getHeaderView(0)
        val titleView = header.findViewById<TextView>(R.id.tvNameSurnameNVHeader)
        val profilePic = header.findViewById<CircleImageView>(R.id.profilePicNVHeader)

        usersRepo.getCurrentUser {
            val profilePicAsString = it.profilePicture.toString()
            val byteArray: ByteArray = Base64.getDecoder().decode(profilePicAsString)
            val bitMapPic = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

            titleView.text = "${it.name} ${it.surname}"
            profilePic.setImageBitmap(bitMapPic)
        }

        menu.setOnClickListener {
            if (!dLayout.isDrawerOpen(Gravity.LEFT)) {
                dLayout.openDrawer(Gravity.LEFT)
            } else {
                dLayout.closeDrawer(Gravity.LEFT)
            }
        }

        filter.setOnClickListener {
            var filterFrag = supportFragmentManager.findFragmentByTag(FILTER_FRAG_TAG)

            if (filterFrag == null)
                supportFragmentManager.beginTransaction()
                    .add(R.id.frameLayoutFilter, FilterResults.newInstance(), FILTER_FRAG_TAG)
                    .commit()
            else {
                supportFragmentManager.beginTransaction().show(filterFrag).commit()
            }
        }

        navView.setNavigationItemSelectedListener {
            if (it.itemId == R.id.miBoleias) {
                supportFragmentManager.beginTransaction().replace(R.id.frameFragment, RidesFragment.newInstance()).commit()

            } else if (it.itemId == R.id.miBoleiasAgendadas) {
                supportFragmentManager.beginTransaction().replace(R.id.frameFragment, MyActiveRidesFragmentFragment.newInstance()).commit()
            } else if (it.itemId == R.id.miChatChannels) {
                // Abrir fragmento dos canais de chat
//                val fragToRemove = supportFragmentManager.findFragmentByTag(RIDES_FRAG_TAG)
//                supportFragmentManager.beginTransaction().remove(fragToRemove!!).commit()
//
//                supportFragmentManager.beginTransaction().add(R.id.frameFragment,
//                    testFragment.newInstance(), CHAT_CHANNELS_FRAG_TAG).commit()

                supportFragmentManager.beginTransaction().replace(R.id.frameFragment, testFragment.newInstance()).commit()

            }
            dLayout.closeDrawer(Gravity.LEFT)
            true
        }

        returnBtn.setOnClickListener {
            val fragToRemove = supportFragmentManager.findFragmentByTag(RIDES_DETAILS_FRAG_TAG)
            val fragToShow = supportFragmentManager.findFragmentByTag(RIDES_FRAG_TAG)

            supportFragmentManager.beginTransaction().remove(fragToRemove!!).commit()
            supportFragmentManager.beginTransaction().show(fragToShow!!).commit()

            returnBtn.visibility = View.GONE
            menu.visibility = View.VISIBLE
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {


            return true
        }

        return super.onOptionsItemSelected(item)
    }
}