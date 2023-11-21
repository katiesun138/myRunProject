package com.example.asn1

//import LocationViewModel
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
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class Map : AppCompatActivity(), LocationListener, OnMapReadyCallback {

    private lateinit var locationManager:LocationManager
//    private lateinit var textView: TextView
    private var map: GoogleMap? = null
    private var markerOptions: MarkerOptions?=null
    private var startMarker: Marker? = null
    private var endMarker: Marker? = null
    private var typeStatsTextView:TextView? = null

    private var mapCentered=false
    private var polylineOptions: PolylineOptions? = null

    private val locationViewModel: LocationViewModel by viewModels()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val saveButton = findViewById<Button>(R.id.btnSave)
        val textStats = findViewById<TextView>(R.id.type_stats)
        val cancelButton = findViewById<Button>(R.id.btnCancel)
//        textView = findViewById<TextView>(R.id.generalText)



        checkPermission()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val input = intent.getStringExtra("inputType")
        val activity = intent.getStringExtra("activityType")

        textStats.text = "Type: ${activity}\n Avg speed: \nCur speed: \n Climb: " +
                "\n Calorie: \n Distance: "

        val intent = Intent(this, TrackingService::class.java)
        startService(intent)

        locationViewModel.getLocationLiveData().observe(this, Observer { location ->
            handleLocationUpdate(location)
        })



        saveButton.setOnClickListener(){
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null && endMarker == null) {
                EndMarker(location)
            }
//            saveMap();
        }
//
//        cancelButton.setOnClickListener(){
//            val intent = Intent()
//            intent.action = TrackingService.ACTION
//            sendBroadcast(intent)
////            cancelMap();
//        }

//        val startIntentLocationService = Intent(this, TrackingService::class.java)
//        startService(startIntentLocationService)

//        val filter = IntentFilter("LOCATION_UPDATE_ACTION")
//        registerReceiver(locationUpdateReceiver, filter)
    }
    private fun handleLocationUpdate(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        Log.d("katieMapActivity", "Handling Location Update - Lat: ${location.latitude}, Long: ${location.longitude}")

        // Update UI or perform any other actions
        updateMapWithLocation(latLng)

    }

    private fun updateMapWithLocation(latLng: LatLng) {

        val cameraUpdateFactory = CameraUpdateFactory.newLatLngZoom(latLng, 17f)
        map?.animateCamera(cameraUpdateFactory)
//        markerOptions?.position(latLng)
//        markerOptions?.let { map?.addMarker(it) }
        polylineOptions?.add(latLng)
        map?.addPolyline(polylineOptions!!)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) initLocationManager()
        }
    }


    private fun initLocationManager() {
        try {
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) return

            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null && startMarker != null){
                onLocationChanged(location)
                Log.d("katie", "in if location !=null statement of initlocationmanager")
            }
            else {
                if (location != null) {
                    StartMarker(location)
                }
                Log.d("katie", "in else statement initlocationmanager, starting initialize of first marker")
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0f, this)


        }catch (e:SecurityException){

        }
    }

    fun StartMarker(location: Location){
        locationViewModel.updateLocation(location)
        val long = location.longitude
        val lat = location.latitude
        val latLng = LatLng(lat, long)
        val cameraUpdateFactory = CameraUpdateFactory.newLatLngZoom(latLng, 17f)
        map?.animateCamera(cameraUpdateFactory)
        markerOptions?.position(latLng)
        markerOptions?.let { map?.addMarker(it) }
        startMarker = map?.addMarker(markerOptions!!)

//        polylineOptions?.add(latLng)
    }
    fun EndMarker(location:Location){
        val long = location.longitude
        val lat = location.latitude
        val latLng = LatLng(lat, long)
        val greenIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
        val cameraUpdateFactory = CameraUpdateFactory.newLatLngZoom(latLng, 17f)
        map?.animateCamera(cameraUpdateFactory)

        markerOptions?.position(latLng)
        markerOptions?.icon(greenIcon)
        markerOptions?.let { map?.addMarker(it) }
        endMarker = map?.addMarker(markerOptions!!)
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

    override fun onLocationChanged(location:Location) {
        locationViewModel.updateLocation(location)
        val long = location.longitude
        val lat = location.latitude
//        textView.text = "Long: $long \n Lat: $lat"
        val latLng = LatLng(lat, long)
        Log.d("katie", "onlocationchanged")
//        if (!mapCentered) {
        val cameraUpdateFactory = CameraUpdateFactory.newLatLngZoom(latLng, 17f)
        map?.animateCamera(cameraUpdateFactory)


        polylineOptions?.add(latLng)
    }


    override fun onMapReady(googleMap:GoogleMap){
        map = googleMap
//        map.mapType = GoogleMap.MAP_TYPE_SATELLITE
        markerOptions = MarkerOptions()
        polylineOptions = PolylineOptions()
        polylineOptions!!.color(Color.DKGRAY)
//        addInitialMarker()
        Log.d("katie", "in onmapready")

        checkPermission()
    }

    override fun onDestroy() {
        super.onDestroy()
        clearMapOverlays()
        resetLocationVariables()
    }

    private fun clearMapOverlays() {
        map?.clear()
    }

    private fun resetLocationVariables() {
        startMarker = null
    }


//    private fun saveMap(){
//        finish()
//    }
//
//    private fun cancelMap(){
//        finish()
//    }

}
