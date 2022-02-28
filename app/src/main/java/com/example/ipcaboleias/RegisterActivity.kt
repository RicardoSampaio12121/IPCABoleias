package com.example.ipcaboleias

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)

        buttonRegister.setOnClickListener{
            register(buttonRegister)
        }
    }


    private fun register(view: View){

        val etEmail = findViewById<EditText>(R.id.editTextEmail)
        val etPassword = findViewById<EditText>(R.id.editTextPassword)
        val etName = findViewById<EditText>(R.id.editTextName)
        val etSurnameName = findViewById<EditText>(R.id.editTextSurname)

        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        val name = etName.text.toString()
        val surname = etSurnameName.text.toString()



        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                var user = NewUser(name, surname, email)
                val firebaseUser: FirebaseUser = task.result!!.user!!
                val uid = firebaseUser.uid

                val database = Firebase.database("https://ipcaboleias-default-rtdb.europe-west1.firebasedatabase.app/")
                val myRef = database.getReference("users/$uid")

                myRef.setValue(user)
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }
}