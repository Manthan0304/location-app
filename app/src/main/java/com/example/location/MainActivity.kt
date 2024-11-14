package com.example.location

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.location.ui.theme.LocationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel : locationviewmodel = viewModel()
            LocationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    myapp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun myapp(viewModel: locationviewmodel) {
    val context = LocalContext.current
    val locationutils = locationutils(context)
    Locationdisplay(locationutils = locationutils, context = context, viewModel = viewModel)
}


@Composable
fun Locationdisplay(locationutils: locationutils, context: Context,
                    viewModel: locationviewmodel) {

val location = viewModel.location.value

    val address = location?.let{
        locationutils.reversegeocodelocation(location)
    }
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                && permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            ) {
                //have access  to location
                locationutils.requestLocationUpdates(viewModel = viewModel)
            } else {
                //get the Permission for location
                val rationalRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                if (rationalRequired) {
                    Toast.makeText(
                        context, "Location Permission Required for this feature to work ",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        context, "Location Permission Required.Please enable it in Settings",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    )


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if(location != null){
            Text(text = "Address: ${location.latitude}, ${location.longitude} \n $address")}
        else{
            Text(
                text = "Location Not Available", modifier = Modifier.padding(16.dp)
            )
        }

        Button(onClick = {
            if (locationutils.haslocationpermission(context)) {
                //TODO: get location
                locationutils.requestLocationUpdates(viewModel = viewModel)
            } else {
                //request location
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
            }
        }) {
            Text(text = "Get Location")
        }
    }
}


