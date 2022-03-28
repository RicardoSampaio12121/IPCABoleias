package com.example.ipcaboleias

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern


class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

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



    }


    private fun register(view: View){

        val etEmail = findViewById<EditText>(R.id.editTextEmail)
        val etPassword = findViewById<EditText>(R.id.editTextPassword)
        val etName = findViewById<EditText>(R.id.editTextName)
        val etSurnameName = findViewById<EditText>(R.id.editTextSurname)
        val etConfirmPassword = findViewById<EditText>(R.id.editTextConfirmPassword)

        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        val name = etName.text.toString()
        val surname = etSurnameName.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()

        if(name.isEmpty()){
            etName.error = "Nome tem que estar preenchido."
            etName.requestFocus()
            return
        }

        if(surname.isEmpty()){
            etSurnameName.error = "Campo Sobrenome tem que estar preenchido."
            etSurnameName.requestFocus()
            return
        }

        if(email.isEmpty()){
            etEmail.error = "Campo Email tem que estar preenchido."
            etEmail.requestFocus()
            return
        }

        // TODO: Verificar se funciona
        if(!Pattern.matches("(a[0-9]+)@alunos.ipca.pt", email) && !Pattern.matches("([a-z]+)@ipca.pt", email)){
            etEmail.error = "Email tem que pertencer ao domínio do IPCA."
            etEmail.requestFocus()
            return
        }

        if(password.isEmpty()){
            etPassword.error = "Campo Password tem que estar preenchido."
            etPassword.requestFocus()
            return
        }

        if(confirmPassword.isEmpty()){
            etConfirmPassword.error = "Tem que confirmar a sua password."
            etConfirmPassword.requestFocus()
            return
        }

        if(confirmPassword != password){
            etConfirmPassword.error = "Passwords não coincidem."
            etConfirmPassword.requestFocus()
            return
        }

        // TODO: Adicionar uma roda de progresso
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                var user = NewUser(name, surname, email)
                val firebaseUser: FirebaseUser = task.result!!.user!!
                val uid = firebaseUser.uid

                val database = Firebase.database("https://ipcaboleias-default-rtdb.europe-west1.firebasedatabase.app/")
                val myRef = database.getReference("users/$uid")

                myRef.setValue(user).addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        Toast.makeText(applicationContext, "Utilizador criado com sucesso!", Toast.LENGTH_LONG).show()
                        finish()
                    }

                }.addOnFailureListener{ exception ->
                    Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }
}