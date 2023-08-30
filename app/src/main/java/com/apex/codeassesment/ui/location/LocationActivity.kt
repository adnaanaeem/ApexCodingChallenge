package com.apex.codeassesment.ui.location

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.apex.codeassesment.R
import com.apex.codeassesment.databinding.ActivityLocationBinding
import com.apex.codeassesment.utils.Constants.USER_LATITUDE_KEY
import com.apex.codeassesment.utils.Constants.USER_LONGITUDE_KEY
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


// TODO (Optional Bonus 8 points): Calculate distance between 2 coordinates using phone's location
class LocationActivity : AppCompatActivity() {
    private lateinit var userLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: ActivityLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val latitudeRandomUser = intent.getStringExtra(USER_LATITUDE_KEY)
        val longitudeRandomUser = intent.getStringExtra(USER_LONGITUDE_KEY)

        binding.locationRandomUser.text =
            getString(R.string.location_random_user, latitudeRandomUser, longitudeRandomUser)

        binding.locationCalculateButton.setOnClickListener {
            Toast.makeText(
                this,
                "TODO (8): Bonus - Calculate distance between 2 coordinates using phone's location",
                Toast.LENGTH_SHORT
            ).show()

            val distance = calculateDistance(userLocation)
            binding.locationDistance.text = getString(R.string.location_result_miles, distance)
        }

        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLastLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            } else {
                Toast.makeText(this, getString(R.string.location_permission_required), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    userLocation = location
                    binding.locationPhone.text = getString(
                        R.string.location_phone,
                        userLocation.latitude.toString(),
                        userLocation.longitude.toString()
                    )
                } else {
                    Toast.makeText(this, getString(R.string.couldnt_retrieve_location), Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun calculateDistance(userLocation: Location): Double {
        val latitudeRandomUser = intent.getStringExtra(USER_LATITUDE_KEY)?.toDoubleOrNull()
        val longitudeRandomUser = intent.getStringExtra(USER_LONGITUDE_KEY)?.toDoubleOrNull()

        val randomUserLocation = Location("")
        randomUserLocation.latitude = latitudeRandomUser ?: 0.0
        randomUserLocation.longitude = longitudeRandomUser ?: 0.0

        // now get distance calculation using given Distance Helper Class
        return DistanceHelper.distance(
            userLocation.latitude,
            userLocation.longitude,
            latitudeRandomUser ?: 0.0,
            longitudeRandomUser ?: 0.0,
            'N' // 'K' for kilometers, 'N' for nautical miles
        )
    }
    companion object {
        private const val LOCATION_PERMISSION_REQUEST = 1
    }
}
