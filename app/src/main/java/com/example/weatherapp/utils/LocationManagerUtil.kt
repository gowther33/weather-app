package com.example.weatherapp.utils

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Task

@SuppressLint("MissingPermission")
class LocationManagerUtil(
    private val context: Context,
    private var timeInterval: Long,
    private var minimalDistance: Float
) : LocationCallback() {

    private var request: LocationRequest
    var locationClient: FusedLocationProviderClient

    init {
        // getting the location client
        locationClient = LocationServices.getFusedLocationProviderClient(context)
        request = createRequest()
    }

    private fun createRequest(): LocationRequest =
        // New builder
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, timeInterval).apply {
            setMinUpdateDistanceMeters(minimalDistance)
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
        }.build()

    fun changeRequest(timeInterval: Long, minimalDistance: Float) {
        this.timeInterval = timeInterval
        this.minimalDistance = minimalDistance
        createRequest()
        stopLocationTracking()
        startLocationTracking()
    }

    fun startLocationTracking() =
        locationClient.requestLocationUpdates(request, this, null)

    fun checkSettings(): Task<LocationSettingsResponse?> {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(request)
        val client = LocationServices.getSettingsClient(context)
        return client.checkLocationSettings(builder.build())
    }

    fun stopLocationTracking() {
//        locationClient.flushLocations()
        locationClient.removeLocationUpdates(this)
    }

    override fun onLocationResult(location: LocationResult) {
        stopLocationTracking()
    }

    override fun onLocationAvailability(availability: LocationAvailability) {
        // TODO: react on the availability change
    }
}