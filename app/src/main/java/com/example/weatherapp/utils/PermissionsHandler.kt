package com.example.weatherapp.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions


class PermissionsHandler(private val activity: Activity) {

     fun requestLocationService() {
        val alertDialog = AlertDialog.Builder(activity)
            .setMessage("GPS is not enabled to get weather update of your locale please enable location")
            .setPositiveButton("Open"
            ) { _, _ ->
                activity.startActivity(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                )
            }
            .setNegativeButton("Close", null)

        alertDialog.show()
    }


     fun askLocPermission(reqCode:Int) {
        val array = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS
        )
        requestPermissions(activity, array, reqCode)
    }

     fun checkPermission():Boolean {
        val resFineLoc = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
        val resCoarseLoc = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
         val resNotify = ActivityCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS)

        return resFineLoc ==
                PackageManager.PERMISSION_GRANTED
                &&
                resCoarseLoc ==
                PackageManager.PERMISSION_GRANTED
                &&
                resNotify == PackageManager.PERMISSION_GRANTED
    }

     fun checkGPS(): Boolean {
        val lm = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (gps_enabled && network_enabled){
            return true
        }
        return  false
    }

}