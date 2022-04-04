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
                // Verificar se já verificou o email

                val user = task.result?.user!!

                if(user.isEmailVerified) {
                    // Redirecionar para a página principal
                    var uid : String = user.uid
                    var intent = Intent(this, RidesActivity::class.java)

                    intent.putExtra("uid", uid)
                    startActivity(intent)
                    finish()

                    Toast.makeText(applicationContext, "Sucesso!", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(applicationContext, "Por favor, verifique o email de confirmação antes de continuar.", Toast.LENGTH_LONG).show()
                }

            }
        }.addOnFailureListener{ exception ->
            Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }





}