package com.example.location

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class locationviewmodel:ViewModel() {
    private val _location = mutableStateOf<locationData?>(null)
    val location : State<locationData?> = _location

    fun updatelocation(newlocation: locationData){
        _location.value = newlocation
    }
}