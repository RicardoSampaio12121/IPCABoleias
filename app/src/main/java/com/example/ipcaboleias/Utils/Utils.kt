package com.example.ipcaboleias.Utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.ipcaboleias.registration.NewUser
import com.example.ipcaboleias.rides.Ride
import com.example.ipcaboleias.rides.RidePresentation
import com.google.type.LatLng
import java.util.*
import java.util.regex.Pattern

class Utils {
    fun isStudent(email: String): Boolean {
        return Pattern.matches("a[0-9]+@alunos.ipca.pt", email)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun stringToBitMapPicture(picture: String): Bitmap {
        val byteArray: ByteArray =
            Base64.getDecoder().decode(picture)

        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    fun getIpcaCampus(endLat: Double): String {
        when (endLat) {
            41.536587 -> {
                return "IPCA Barcelos"
            }
            41.542142 -> {
                return "IPCA Braga"
            }
            41.507823 -> {
                return "IPCA Guimarães"
            }
            41.440063 -> {
                return "IPCA Famalicão"
            }
        }
        return ""
    }

    fun newRidePresentationObject(ride: Ride, user: NewUser): RidePresentation {
        return RidePresentation(
            ride.uid,
            user.name,
            user.email,
            "${user.carBrand} ${user.carModel}",
            user.carColor!!,
            user.profilePicture!!,
            ride.dateTime,
            ride.startLatitute,
            ride.startLongitude,
            ride.endLatitute,
            ride.endLongitude,
            ride.type,
            ride.places,
            ride.description,
            ride.acceptAlunos,
            ride.acceptDoc,
            ride.uniqueDrive,
            ride.price
        )
    }

    fun getLocation(context: Context, latitude: Double, longitude: Double): Address? {
        val addresses: MutableList<Address>
        val geocoder = Geocoder(context, Locale.ENGLISH)

        addresses = geocoder.getFromLocation(latitude, longitude, 1)

        if (addresses.size == 0) {
            return null
        }
        return addresses[0]
    }

    fun calculatePrice(start: com.google.android.gms.maps.model.LatLng, end: com.google.android.gms.maps.model.LatLng): Float{
        val results = FloatArray(1)
        Location.distanceBetween(start.latitude, start.longitude, end.latitude, end.longitude, results)

        val distanceInKm: Float = results[0] / 1000

        return distanceInKm * 0.3f
    }
}