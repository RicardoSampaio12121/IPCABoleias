package com.example.ipcaboleias

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.example.ipcaboleias.databinding.FragmentRegister3Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineStart
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.regex.Pattern

class register3Fragment : Fragment(R.layout.fragment_register3) {

    private lateinit var mAuth: FirebaseAuth
    private val model: NewUserViewModel by activityViewModels()

    private var _binding: FragmentRegister3Binding? = null
    private val binding get() = _binding!!
    private var picture: Bitmap? = null

    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentRegister3Binding.bind(view)
        mAuth = FirebaseAuth.getInstance()


        binding.apply {
            btnTakePicture.setOnClickListener {
               var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, 101)

                picturaView.setImageBitmap(picture)
            }

            btnViewPicture.setOnClickListener{
                picturaView.setImageBitmap(picture)
            }

            btnRegister.setOnClickListener {
                register()
            }



        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 101){
            picture = data?.getParcelableExtra<Bitmap>("data")
            model.setPicture(data!!.getParcelableExtra<Bitmap>("data"))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun register(){

//        val etEmail = findViewById<EditText>(R.id.editTextEmail)
//        val etPassword = findViewById<EditText>(R.id.editTextPassword)
//        val etName = findViewById<EditText>(R.id.editTextName)
//        val etSurnameName = findViewById<EditText>(R.id.editTextSurname)
//        val etConfirmPassword = findViewById<EditText>(R.id.editTextConfirmPassword)
//
//        val email = etEmail.text.toString()
//        val password = etPassword.text.toString()
//        val name = etName.text.toString()
//        val surname = etSurnameName.text.toString()
//        val confirmPassword = etConfirmPassword.text.toString()
//
//        if(name.isEmpty()){
//            etName.error = "Nome tem que estar preenchido."
//            etName.requestFocus()
//            return
//        }
//
//        if(surname.isEmpty()){
//            etSurnameName.error = "Campo Sobrenome tem que estar preenchido."
//            etSurnameName.requestFocus()
//            return
//        }
//
//        if(email.isEmpty()){
//            etEmail.error = "Campo Email tem que estar preenchido."
//            etEmail.requestFocus()
//            return
//        }
//
//        // TODO: Verificar se funciona
//        if(!Pattern.matches("(a[0-9]+)@alunos.ipca.pt", email) && !Pattern.matches("([a-z]+)@ipca.pt", email)){
//            etEmail.error = "Email tem que pertencer ao domínio do IPCA."
//            etEmail.requestFocus()
//            return
//        }
//
//        if(password.isEmpty()){
//            etPassword.error = "Campo Password tem que estar preenchido."
//            etPassword.requestFocus()
//            return
//        }
//
//        if(confirmPassword.isEmpty()){
//            etConfirmPassword.error = "Tem que confirmar a sua password."
//            etConfirmPassword.requestFocus()
//            return
//        }
//
//        if(confirmPassword != password){
//            etConfirmPassword.error = "Passwords não coincidem."
//            etConfirmPassword.requestFocus()
//            return
//        }

        // TODO: Adicionar uma roda de progresso

        // Criar utilizador
        mAuth.createUserWithEmailAndPassword(model.getEmail(), model.getPassword()).addOnCompleteListener { task ->
            if(task.isSuccessful){

                var user = NewUser()
                user.name = model.getName()
                user.surname = model.getSurname()
                user.email = model.getEmail()
                user.carBrand = model.getCarBrand()
                user.carModel = model.getCarModel()
                user.carColor = model.getCarColor()
                val pictureAsString = BitMapToString(model.getPicture())
                user.profilePicture = pictureAsString

                // Enviar email de verificação
                val firebaseUser: FirebaseUser = task.result!!.user!!

                firebaseUser.sendEmailVerification()
                    .addOnCompleteListener { taskk ->
                        if(taskk.isSuccessful)
                            Toast.makeText(activity, "Enviamos um email de confirmação para o seu endereço", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(activity, "Falha ao enviar email de verificação para " + e.message, Toast.LENGTH_LONG).show()
                    }

                // Guardar dados na realtime database
                val uid = firebaseUser.uid

                val database = Firebase.database("https://ipcaboleias-default-rtdb.europe-west1.firebasedatabase.app/")
                val myRef = database.getReference("users/$uid")

                myRef.setValue(user).addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        Toast.makeText(activity, "Utilizador criado com sucesso!", Toast.LENGTH_LONG).show()
                        activity?.finish()
                    }

                }.addOnFailureListener{ exception ->
                    Toast.makeText(activity, exception.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(activity,exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun BitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.getEncoder().encodeToString(b)
    }



    companion object {
        @JvmStatic
        fun newInstance() =
            register3Fragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}