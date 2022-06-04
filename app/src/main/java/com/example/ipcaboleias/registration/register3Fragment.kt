package com.example.ipcaboleias.registration

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.example.ipcaboleias.R
import com.example.ipcaboleias.ViewModels.NewUserViewModel
import com.example.ipcaboleias.databinding.FragmentRegister3Binding
import com.example.ipcaboleias.firebaseRepository.UsersRepository
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream
import java.util.*

class register3Fragment : Fragment(R.layout.fragment_register3) {

    private lateinit var mAuth: FirebaseAuth
    private val model: NewUserViewModel by activityViewModels()

    private var _binding: FragmentRegister3Binding? = null
    private val binding get() = _binding!!
    private var picture: Bitmap? = null

    val REQUEST_IMAGE_CAPTURE = 1


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentRegister3Binding.bind(view)
        mAuth = FirebaseAuth.getInstance()


        binding.apply {
            btnTakePicture.setOnClickListener {
               var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, 101)

                picturaView.setImageBitmap(picture)
            }

//            btnViewPicture.setOnClickListener{
//                picturaView.setImageBitmap(picture)
//            }

            btnRegister.setOnClickListener {
                register()
                requireActivity().finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val picView = requireActivity().findViewById<CircleImageView>(R.id.picturaView)

        if(requestCode == 101){
            picture = data?.getParcelableExtra<Bitmap>("data")
            picView.setImageBitmap(data?.getParcelableExtra<Bitmap>("data"))
            model.setPicture(data!!.getParcelableExtra<Bitmap>("data"))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun register(){

        var user = NewUser()
        user.name = model.getName()
        user.surname = model.getSurname()
        user.email = model.getEmail()
        user.carBrand = model.getCarBrand()
        user.carModel = model.getCarModel()
        user.carColor = model.getCarColor()
        user.carPlate = model.getCarPlate()
        val pictureAsString = BitMapToString(model.getPicture())
        user.profilePicture = pictureAsString

        // Criar utilizador

        val userRepo = UsersRepository(requireContext())
        userRepo.userRegistration(user, model.getPassword())
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