package com.example.projecthack4pan

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
import android.provider.Settings
import android.view.View
import androidx.fragment.app.Fragment


class Amenities : AppCompatActivity(), View.OnClickListener {

    private var mapCount = 0
    private lateinit var btnHospital:Button
    private lateinit var btnFire:Button
    private lateinit var btnFuel:Button
    private lateinit var btnPolice:Button
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
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.police->{
                val intent =  Intent(this, AmenityMap::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra("amenity","police")
                startActivity(intent)
            }
            R.id.fuel->{
                val intent =  Intent(this, AmenityMap::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra("amenity","fuel")
                startActivity(intent)
            }
            R.id.hospitals->{
                val intent =  Intent(this, AmenityMap::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra("amenity","hospitals")
                startActivity(intent)
            }
            R.id.fire->{
                val intent =  Intent(this, AmenityMap::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra("amenity","fire")
                startActivity(intent)
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