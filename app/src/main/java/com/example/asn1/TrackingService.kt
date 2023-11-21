package com.example.asn1

import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.location.LocationListener

import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


////class TrackingService: Service() {
////    private lateinit var myBroadcastReceiver: MyBroadcastReceiver
////    val locationLiveData = MutableLiveData<Location>()
////    private lateinit var notificationManager:NotificationManager
////    private val CHANNEL_ID = "katie channel id"
////    private val NOTIFY_ID = 3
////    val NOTIFICATION_ID = 777
////    companion object{
////        val ACTION = "STOP SERVICE"
////    }
////
////    override fun onCreate() {
////        super.onCreate()
////        println("katie onCreate serive called")
////        myBroadcastReceiver = MyBroadcastReceiver()
////        val intentFilter = IntentFilter()
////        intentFilter.addAction(ACTION)
////        registerReceiver(myBroadcastReceiver, intentFilter)
////        showNotification()
////    }
////
////    private fun showNotification(){
////        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
////        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
////        notificationBuilder.setContentText("katie title")
////        notificationBuilder.setContentText("katie content text")
////        notificationBuilder.setSmallIcon(R.drawable.icon_service)
////        val notification = notificationBuilder.build()
////
////        if (Build.VERSION.SDK_INT > 26){
////            val notificationChannel = NotificationChannel(CHANNEL_ID, "channel name",
////                NotificationManager.IMPORTANCE_DEFAULT)
////            notificationManager.createNotificationChannel(notificationChannel)
////        }
////        notificationManager.notify(NOTIFY_ID, notification)
////
////    }
////
////    override fun onDestroy() {
////        super.onDestroy()
////        println("katie onDestroy called")
////        cleanUpTasks()
////    }
////
////    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
////        println("katie onstartcommand() called, startID: $startId")
////        return START_NOT_STICKY
////    }
////
////    private fun cleanUpTasks(){
////        notificationManager.cancel(NOTIFICATION_ID)
////    }
////    override fun onBind(intent: Intent?): IBinder? {
////        return null
////    }
////
////    override fun onUnbind(intent:Intent?): Boolean {
////        println("katie unbind() called")
////        return true
////    }
////
//////    fun onLocationUpdateReceived(lat:Double, long:Double){
//////        sendLocationUpdate(lat, long)
//////    }
////
////    inner class MyBroadcastReceiver: BroadcastReceiver(){
////        override fun onReceive(context: Context, intent:Intent){
////            println("katie onReceive service")
////            stopSelf()
////            unregisterReceiver(myBroadcastReceiver)
////            notificationManager.cancel(NOTIFY_ID)
////        }
////    }
////
////    private fun sendLocationUpdate(lat:Double, long:Double){
////        val intent = Intent("LOCATION_UPDATE_ACTION")
////        intent.putExtra("lat", lat)
////        intent.putExtra("long", long)
////        sendBroadcast(intent)
////    }
////}
////
////
//import android.annotation.SuppressLint
//import android.app.Service
//import android.content.Intent
//import android.location.Location
//import android.os.Binder
//import android.os.IBinder
//import androidx.lifecycle.MutableLiveData
//import androidx.localbroadcastmanager.content.LocalBroadcastManager
//import com.google.android.gms.location.*
//
//class LocationTrackingService : Service() {
//
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//    private lateinit var locationRequest: LocationRequest
//
//    private val binder = LocalBinder()
//    private val locationLiveData = MutableLiveData<Location>()
//
//
//    inner class LocalBinder : Binder() {
//        fun getService(): LocationTrackingService = this@LocationTrackingService
//    }
//
//    override fun onBind(intent: Intent?): IBinder? {
//        return binder
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//
//        locationRequest = LocationRequest.create()
//            .setInterval(5000) // Update interval in milliseconds
//            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//
//        startLocationUpdates()
//    }
//
//    @SuppressLint("MissingPermission")
//    private fun startLocationUpdates() {
//        val locationCallback = object : LocationCallback() {
//            override fun onLocationResult(p0: LocationResult) {
//                p0?.lastLocation?.let { location ->
//                    locationLiveData.postValue(location)
//                }
//            }
//        }
//
//        fusedLocationClient.requestLocationUpdates(
//            locationRequest,
//            locationCallback,
//            null /* Looper */
//        )
//    }
//
//    private val locationCallback = object : LocationCallback() {
//        override fun onLocationResult(p0: LocationResult) {
//            p0?.lastLocation?.let { location ->
//                // Broadcast the new location to activities
//                broadcastLocation(location)
//                println("Katie new location $location")
//            }
//        }
//    }
//
//    private fun broadcastLocation(location: Location) {
//        val intent = Intent("LOCATION_UPDATE_ACTION")
//        intent.putExtra("lat", location.latitude)
//        intent.putExtra("long", location.longitude)
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        stopLocationUpdates()
//    }
//
//    private fun stopLocationUpdates() {
//        fusedLocationClient.removeLocationUpdates(locationCallback)
//    }
//}

class TrackingService : Service(), LocationListener {

    private val locationLiveData = MutableLiveData<Location>()
    private lateinit var locationViewModel: LocationViewModel

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    fun getLocationLiveData(): LiveData<Location> {
        return locationLiveData
    }

    override fun onLocationChanged(location: Location) {
//        GlobalScope.launch {
            updateLocation(location)
//        }
    }

    private fun updateLocation(location: Location) {
        locationViewModel.updateLocation(location)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
