package ru.oliverhd.geolocations

import com.google.android.gms.maps.model.LatLng

data class Marker(
    var name: String?,
    var latLong: LatLng,
    var annotation: String?
)