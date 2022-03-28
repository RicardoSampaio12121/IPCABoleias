package com.example.ipcaboleias

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        val tvRegisterUser = findViewById<TextView>(R.id.textViewRegistar)

         tvRegisterUser.setOnClickListener{
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val btSignIn = findViewById<Button>(R.id.buttonEntrar)
        val etEmail = findViewById<EditText>(R.id.editTextEmail)
        val etPassword = findViewById<EditText>(R.id.editTextPassword)

        btSignIn.setOnClickListener{
            login(etEmail, etPassword)
        }
    }

    // TODO: Adicionar uma rodinha enquanto carrega
    private fun login(etEmail:EditText, etPassword:EditText){

        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
            if(task.isSuccessful){
                // Redirecionar para a página principal
                var intent = Intent(this, RidesActivity::class.java)
                startActivity(intent)
                finish()

                Toast.makeText(applicationContext, "Sucesso!", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener{ exception ->
            Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }





}