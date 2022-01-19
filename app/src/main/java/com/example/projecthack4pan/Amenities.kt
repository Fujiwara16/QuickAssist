package com.example.projecthack4pan

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.ButtCap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.content.Context

import android.content.Intent

import android.content.DialogInterface

import android.location.LocationManager
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.widget.Toast
import com.google.android.gms.common.api.GoogleApi
import java.lang.Exception

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient


class Amenities : AppCompatActivity(), View.OnClickListener {

    private var mapCount = 0
    private lateinit var btnHospital:Button
    private lateinit var btnFire:Button
    private lateinit var btnFuel:Button
    private lateinit var btnPolice:Button
    private var REQUEST_CODE = 1
    private var lat:Double = 0.0
    private var long:Double = 0.0
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var gpsStatus: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amenities)
        btnHospital = findViewById(R.id.hospitals)
        btnFire = findViewById(R.id.fire)
        btnPolice = findViewById(R.id.police)
        btnFuel = findViewById(R.id.fuel)
        checkLocationServices()
        btnHospital.setOnClickListener(this)
        btnFuel.setOnClickListener(this)
        btnFire.setOnClickListener(this)
        btnPolice.setOnClickListener(this)

    }
//
//    private fun police() {
//        mapCount = 1
//        val mapFragment1 = supportFragmentManager
//            .findFragmentById(R.id.mapPolice) as SupportMapFragment?
//        mapFragment1!!.getMapAsync(this)
//    }



    @SuppressLint("MissingPermission")
    private fun checkLocationServices() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if(!gpsStatus){
        //    Toast.makeText(this,"Please enable Location ")
            AlertDialog.Builder(this)
                .setMessage("Gps not enabled")
                .setPositiveButton("Open Settings",
                    DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                        this.startActivity(
                            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        )
                    })
                .setNegativeButton("Cancel", null)
                .show()
        }
        //checking permissions
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE)

                // REQUEST_CODE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }

        fusedLocationClient?.lastLocation
            ?.addOnSuccessListener { location: Location?->
                if (location != null) {
                    // use your location object
                    // get latitude , longitude and other info from this
                    //Toast.makeText(this,"working",Toast.LENGTH_SHORT).show()
                    lat = location.latitude
                    long = location.longitude
                }

            }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.police->{
                val gmmIntentUri = Uri.parse("geo:$lat,$long?q=Police Stations")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
            R.id.fuel->{
                val gmmIntentUri = Uri.parse("geo:$lat,$long?q=Fuel Stations")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
            R.id.hospitals->{
                val gmmIntentUri = Uri.parse("geo:$lat,$long?q=Hospitals")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
            R.id.fire->{
                val gmmIntentUri = Uri.parse("geo:$lat,$long?q=Fire Stations")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
        }
    }

//    override fun onMapReady(googleMap: GoogleMap) {
//
//        if (mapCount == 1) {
//            mapPolice = googleMap
//            val vannes = LatLng(47.66, -2.75)
//            mapPolice.addMarker(MarkerOptions().position(vannes).title("Vannes"))
//            mapPolice.moveCamera(CameraUpdateFactory.newLatLng(vannes))
//        } else if (mapCount == 2) {
//            mapHospital = googleMap
//            val bordeaux = LatLng(44.833328, -0.56667)
//            mapHospital.addMarker(MarkerOptions().position(bordeaux).title("Bordeaux"))
//            mapHospital.moveCamera(CameraUpdateFactory.newLatLng(bordeaux))
//        }else if (mapCount == 3) {
//            mapFire = googleMap
//            val bordeaux = LatLng(44.833328, -0.56667)
//            mapFire.addMarker(MarkerOptions().position(bordeaux).title("Bordeaux"))
//            mapFire.moveCamera(CameraUpdateFactory.newLatLng(bordeaux))
//        }else if (mapCount == 4) {
//            mapFuel = googleMap
//            val bordeaux = LatLng(44.833328, -0.56667)
//            mapFuel.addMarker(MarkerOptions().position(bordeaux).title("check"))
//            mapFuel.moveCamera(CameraUpdateFactory.newLatLng(bordeaux))
//        }
//    }

//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//
//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(
//            MarkerOptions()
//                .position(sydney)
//                .title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//    }

}