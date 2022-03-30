package ru.oliverhd.geolocations

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import ru.oliverhd.geolocations.MarkerListFragment.Companion.MARKER_LIST
import ru.oliverhd.geolocations.databinding.FragmentMapsBinding
import java.io.IOException

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding: FragmentMapsBinding
        get() = _binding!!
    private var mapFragment: SupportMapFragment? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var location: LatLng
    private var address: String = ""
    private lateinit var map: GoogleMap
    private val initialPlace = LatLng(SPB_LATITUDE, SPB_LONGITUDE)

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        showMarkers()
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(initialPlace))
        googleMap.setOnMapLongClickListener {
            getAddress(it)
            addMarkerToList(it)
            showMarkers()
        }
    }

    private fun addMarkerToList(latLng: LatLng) {
        val marker = Marker(address, latLng, null)
        MARKER_LIST.add(marker)
    }

    private val myLocation = OnMapReadyCallback { googleMap ->
        getAddress(location)
        googleMap.addMarker(
            MarkerOptions()
                .position(location)
                .title(address)
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        checkLocationPermission()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        binding.markerListButton.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.container, MarkerListFragment.newInstance())
                .addToBackStack("")
                .commit()
        }
        binding.locationFAB.setOnClickListener {
            mapFragment?.getMapAsync(myLocation)
        }
    }

    @SuppressLint("MissingPermission")
    private fun checkLocationPermission() {
        location = LatLng(0.0, 0.0)
        val task: Task<Location> = fusedLocationProviderClient.lastLocation
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101
            )
            return
        }
        task.addOnSuccessListener {
            location = LatLng(it.latitude, it.longitude)
        }
    }

    private fun getAddress(location: LatLng) {
        context?.let {
            val geoCoder = Geocoder(it)

            try {
                val addresses =
                    geoCoder.getFromLocation(location.latitude, location.longitude, 1)
                address = if (addresses.size > 0) {
                    addresses[0].getAddressLine(0)
                } else
                    "Unknown"
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    private fun showMarkers() {
        map.clear()
        MARKER_LIST.forEach {
            map.addMarker(
                MarkerOptions()
                    .position(it.latLong)
                    .title(it.name)
            )
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        fun newInstance(): MapsFragment = MapsFragment()
        private const val SPB_LATITUDE = 59.9342802
        private const val SPB_LONGITUDE = 30.3350986
    }
}