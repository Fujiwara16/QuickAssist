package com.example.projecthack4pan

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.lifecycle.Transformations.map
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback

import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.view.animation.Animation

import android.view.animation.LinearInterpolator

import android.view.animation.AlphaAnimation
import android.widget.Toast
import android.location.LocationManager

import android.app.Activity
import android.content.Context

import android.content.pm.PackageManager
import android.location.LocationRequest
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.requestLocationUpdates
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import android.Manifest.permission
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.location.Location

import com.google.android.gms.tasks.OnCompleteListener

import androidx.core.content.ContextCompat





class HomePage : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {
    private lateinit var mMap: GoogleMap

    private lateinit var btnVolunteer:Button
    private lateinit var btnAmenities:Button
    private lateinit var btnDistress:Button
    private lateinit var btnAlert:Button
    private lateinit var userId:String
    private var mAuth: FirebaseAuth? = null
    private var counter = 0
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    // globally declare LocationRequest
    private lateinit var locationRequest: LocationRequest
    // globally declare LocationCallback
    private lateinit var locationCallback: LocationCallback
    private val REQUEST_CODE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        btnAmenities = findViewById(R.id.amenities)
        btnDistress = findViewById(R.id.distress)
        btnAlert = findViewById(R.id.alert)
        btnVolunteer = findViewById(R.id.volunteer)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        initialiseFirebase()
        checkDistress()
        btnAmenities.setOnClickListener(this)
        btnDistress.setOnClickListener(this)
        btnVolunteer.setOnClickListener(this)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    private fun checkDistress() {
        userId = mAuth!!.currentUser!!.uid
        mDatabase!!.reference.child("distress").child(userId).get().addOnSuccessListener {
          if(it.exists()){
            animateAlert()
            counter = 1
        }
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(
            MarkerOptions()
            .position(sydney)
            .title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun initialiseFirebase(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")

    }
    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.volunteer ->{

            }
            R.id.distress ->{
                raiseCall()
            }
            R.id.amenities ->{
                 startActivity(Intent(this,Amenities::class.java))
            }
        }
    }

    private fun raiseCall() {
        if(counter == 0) {
            getLocationUpdates()
            counter = 1
            animateAlert()
        }
        else if(counter == 1){
            Toast.makeText(this,"Call Suspended",Toast.LENGTH_SHORT).show()
            mDatabase!!.reference.child("distress").child(userId).setValue(null)
            btnAlert.clearAnimation()
            btnAlert.visibility = View.INVISIBLE
            counter = 0
        }

    }
    //store latitude and longitude
    private fun getLocationUpdates()
    {
        val db = mDatabase!!.reference.child("distress")
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

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location:Location?->
                if (location != null) {
                    // use your location object
                    // get latitude , longitude and other info from this
                    //Toast.makeText(this,"working",Toast.LENGTH_SHORT).show()
                    db.child(userId).child("latitude").setValue(location.latitude)
                    db.child(userId).child("longitude").setValue(location.longitude)

                }

            }

    }


    private fun animateAlert() {
        btnAlert.visibility = View.VISIBLE
        val mAnimation: Animation = AlphaAnimation(1f,0f)
        mAnimation.duration = 200
        mAnimation.interpolator = LinearInterpolator()
        mAnimation.repeatCount = Animation.INFINITE
        mAnimation.repeatMode = Animation.REVERSE
        btnAlert.startAnimation(mAnimation)
    }
}
