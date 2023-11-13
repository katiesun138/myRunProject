package com.example.asn1

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class Map : AppCompatActivity(), LocationListener, OnMapReadyCallback {

    private lateinit var locationManager:LocationManager
//    private lateinit var textView: TextView
    private lateinit var map: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

//        val saveButton = findViewById<Button>(R.id.saveButton)
//        val cancelButton = findViewById<Button>(R.id.cancelButton)
//        textView = findViewById<TextView>(R.id.generalText)


        checkPermission()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



//        saveButton.setOnClickListener(){
//            saveMap();
//        }
//
//        cancelButton.setOnClickListener(){
//            cancelMap();
//        }
    }

    override fun onMapReady(googleMap:GoogleMap){
        map = googleMap
        map.mapType = GoogleMap.MAP_TYPE_SATELLITE
    }

    private fun initLocationManager() {
        try {
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) return

            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null){
                onLocationChanged(location)
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0f, this)


        }catch (e:SecurityException){

        }
    }

    override fun onLocationChanged(location:Location){
        val long = location.longitude
        val lat = location.latitude
//        textView.text = "Long: $long \n Lat: $lat"
        val latLng = LatLng(lat, long)
        val cameraUpdateFactory = CameraUpdateFactory.newLatLngZoom(latLng, 17f,  )
        map.animateCamera(cameraUpdateFactory)
    }

    override fun onDestroy(){
        super.onDestroy()
        if (locationManager != null){
            locationManager.removeUpdates(this)
        }
    }

    fun checkPermission() {
        if (Build.VERSION.SDK_INT < 23) return
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        }
        else{
            initLocationManager()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) initLocationManager()
        }
    }
    private fun saveMap(){
        finish()
    }

    private fun cancelMap(){
        finish()
    }
}