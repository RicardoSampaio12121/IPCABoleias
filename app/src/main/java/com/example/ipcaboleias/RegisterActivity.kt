package com.example.ipcaboleias

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import com.example.ipcaboleias.ViewModels.NewUserViewModel
import com.example.ipcaboleias.registration.NewUser
import com.example.ipcaboleias.registration.Register1FragmentFragment
import com.google.firebase.auth.FirebaseAuth


class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    var newUser = NewUser()
    private val model: NewUserViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var REGISTER1_FRAG_TAG = "register1_frag_tag"
        var REGISTER2_FRAG_TAG = "register2_frag_tag"


        mAuth = FirebaseAuth.getInstance()

        //TODO: Set custom animations
        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, Register1FragmentFragment.newInstance(), REGISTER1_FRAG_TAG).commit()



        var btnReturn = findViewById<ImageButton>(R.id.btnReturn)

        btnReturn.setOnClickListener{
            var frag1 = supportFragmentManager.findFragmentByTag(REGISTER1_FRAG_TAG)
            var frag2 = supportFragmentManager.findFragmentByTag(REGISTER2_FRAG_TAG)

            if(frag1 != null && frag1.isVisible){
                finish()
            }
            else if(frag2 != null && frag2.isVisible) {
                supportFragmentManager.beginTransaction().show(frag1!!).commit()
                supportFragmentManager.beginTransaction().hide(frag2).commit()
            }
        }

        var fragmentContainer = findViewById<FragmentContainerView>(R.id.fragmentContainer)

        fragmentContainer.setOnClickListener {
            Toast.makeText(applicationContext, model.getName(), Toast.LENGTH_SHORT).show()
        }



    }



}