package com.example.asn1

import androidx.lifecycle.ViewModel
import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng

//class LocationViewModel (application: Application):AndroidViewModel(application){
//
//    private val locationLiveData = MutableLiveData<Location>()
//    private val pathLiveData = MutableLiveData<List<LatLng>>()
//
//    init {
//        // Initialize pathLiveData with an empty list
//        pathLiveData.value = mutableListOf()
//    }
//
//    fun getLocationLiveData(): LiveData<Location> = locationLiveData
//
//    // Function to get the LiveData for the entire path
//    fun getPathLiveData(): LiveData<List<LatLng>> = pathLiveData
//
//    fun updateLocation(location: Location) {
//        locationLiveData.postValue(location)
//
//        // Update the pathLiveData with the new location
//        val newPath = pathLiveData.value?.toMutableList()
//        newPath?.add(LatLng(location.latitude, location.longitude))
//        pathLiveData.postValue(newPath)
//    }
//}

class LocationViewModel : ViewModel() {

    private val locationLiveData = MutableLiveData<Location>()

    fun updateLocation(location: Location) {
        locationLiveData.postValue(location)
        Log.d("KatieTrackingService", "New Location - Lat: ${location.latitude}, Long: ${location.longitude}")

    }

    fun getLocationLiveData(): LiveData<Location> {
        return locationLiveData
    }
}