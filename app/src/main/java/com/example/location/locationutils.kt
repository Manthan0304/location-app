package com.example.location

import android.content.Context
import androidx.core.content.ContextCompat
import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import java.util.Locale


class locationutils(val context: Context) {

    private val _fusedlocationclient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun requestLocationUpdates(viewModel: locationviewmodel) {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let {
                    val location = locationData(latitude = it.latitude, longitude = it.longitude)
                    viewModel.updatelocation(location)
                }
            }
        }
        val locationrequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build()
        _fusedlocationclient.requestLocationUpdates(
            locationrequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }


    fun haslocationpermission(context: Context): Boolean {

        return ContextCompat.checkSelfPermission(
            context,
            permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(
                    context,
                    permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    fun reversegeocodelocation(location: locationData): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val coordinate = LatLng(location.latitude, location.longitude)
        val adresses: MutableList<Address>? =
            geocoder.getFromLocation(coordinate.latitude, coordinate.longitude, 1)
        return if(adresses?.isNotEmpty() == true){
            adresses[0].getAddressLine(0)
        }else{
            "Address Not Found"
        }
    }
}









