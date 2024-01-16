package com.dispatch.tripsheet.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import java.util.*

class MyLocation {

    internal lateinit var timer1: Timer
    internal var lm: LocationManager? = null
    internal lateinit var locationResult: LocationResult
    internal var gps_enabled = false

    internal var locationListenerGps: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            timer1.cancel()
            locationResult.gotLocation(location)
            lm!!.removeUpdates(this)
            log("GPS onLocationChanged")
        }

        override fun onProviderDisabled(provider: String) {
            log("GPS onProviderDisabled")
        }

        override fun onProviderEnabled(provider: String) {
            log("GPS onProviderEnabled")
        }

        @Deprecated("Deprecated in Java")
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            log("GPS onStatusChanged")
        }
    }

    fun getLocation(context: Context, result: LocationResult): Boolean {
        locationResult = result
        if (lm == null)
            lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        try {
            gps_enabled = lm!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
            log("Exception checking GPS provider: ${ex.message}")
        }

        if (!gps_enabled) {
            log("GPS provider not enabled")
            return false
        }

        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 111)
        }

        if (gps_enabled) {
            lm!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 1000f, locationListenerGps)
            log("GPS provider requested updates")
        }

        timer1 = Timer()
        timer1.schedule(GetLastLocation(context), 20000)
        log("Timer scheduled")
        return true
    }

    internal inner class GetLastLocation(var context: Context) : TimerTask() {
        override fun run() {
            lm!!.removeUpdates(locationListenerGps)
            log("Removed GPS updates")

            var gps_loc: Location? = null

            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(context as Activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),111)
            }

            if (gps_enabled) {
                gps_loc = lm!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                log("Got last known location from GPS")
            }

            if (gps_loc != null) {
                // Use runOnUiThread to ensure the observe is on the main thread
                (context as Activity).runOnUiThread {
                    locationResult.gotLocation(gps_loc)
                }
                log("LocationResult.gotLocation called")
            } else {
                // Use runOnUiThread to ensure the observe is on the main thread
                (context as Activity).runOnUiThread {
                    locationResult.gotLocation(null)
                }
                log("LocationResult.gotLocation called with null")
            }
        }
    }
    abstract class LocationResult {
        abstract fun gotLocation(location: Location?)
    }

    private fun log(message: String) {
        // You can replace this with proper logging, e.g., Log.d(TAG, message)
        println(message)
    }
}
