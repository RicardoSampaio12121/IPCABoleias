package com.example.ipcaboleias

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.ipcaboleias.firebaseRepository.Callbacks.userLoginCallback
import com.example.ipcaboleias.firebaseRepository.UsersRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        val tvRegisterUser = findViewById<TextView>(R.id.textViewRegistar)

        tvRegisterUser.setOnClickListener {
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val btSignIn = findViewById<Button>(R.id.buttonEntrar)
        val etEmail = findViewById<EditText>(R.id.editTextEmail)
        val etPassword = findViewById<EditText>(R.id.editTextPassword)

        btSignIn.setOnClickListener {
            login(etEmail, etPassword)
        }
    }

    // TODO: Adicionar uma rodinha enquanto carrega
    private fun login(etEmail: EditText, etPassword: EditText) {

        val intent = Intent(this, RidesActivity::class.java)

        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        val userRepo = UsersRepository(this)

        userRepo.userLogin(email, password, object : userLoginCallback {
            override fun onCallback(uid: String?) {
//                userRepo.storeTokenIfFirstLogin()

                FirebaseMessaging.getInstance().token.addOnSuccessListener {
                    userRepo.updateUserToken(it)
                }

                intent.putExtra("uid", uid)
                startActivity(intent)
                finish()
            }
        })

    }
}