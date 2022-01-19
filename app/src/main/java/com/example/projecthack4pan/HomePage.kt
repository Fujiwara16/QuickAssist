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
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.location.Location
import android.provider.Settings

import com.google.android.gms.tasks.OnCompleteListener

import androidx.core.content.ContextCompat





class HomePage : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {
    private lateinit var mMap: GoogleMap

    private lateinit var btnVolunteer:Button
    private lateinit var btnAmenities:Button
    private lateinit var btnDistress:Button
    private lateinit var btnAlert:Button
    private lateinit var btnLogOut:Button
    private lateinit var btnContact:Button
    private lateinit var userId:String
    private var mAuth: FirebaseAuth? = null
    private var counter = 0
    private var mapCounter = 0
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private lateinit var distressArray:ArrayList<LatLng>

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    // globally declare LocationRequest
    private lateinit var locationRequest: LocationRequest
    // globally declare LocationCallback
    private lateinit var locationCallback: LocationCallback
    private val REQUEST_CODE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        btnLogOut = findViewById(R.id.logout)
        btnAmenities = findViewById(R.id.amenities)
        btnDistress = findViewById(R.id.distress)
        btnAlert = findViewById(R.id.alert)
        btnVolunteer = findViewById(R.id.volunteer)
        btnContact = findViewById(R.id.contacts)
        distressArray = arrayListOf()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        initialiseFirebase()
        checkDistressUser()
        btnLogOut.setOnClickListener(this)
        btnContact.setOnClickListener(this)
        btnAmenities.setOnClickListener(this)
        btnDistress.setOnClickListener(this)
        btnVolunteer.setOnClickListener(this)
        initialiseMap()
    }

    private fun initialiseMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

    }

    private fun checkDistressUser() {
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
        //distressArray.add(LatLng(37.42342342342342,-122.08395287867832))
       // Toast.makeText(this,"$mapCounter",Toast.LENGTH_SHORT).show()
        if(mapCounter == 1)
        {
            for(i in distressArray) {
                mMap.addMarker(
                    MarkerOptions()
                        .position(i)
                        .title("HELP!!"))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(i))
            }

        }
        else if(mapCounter == 0 ) {
            mMap.clear()
        }

    }

    private fun initialiseFirebase(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")

    }
    override fun onClick(p0: View?) {
        when(p0?.id) {

            R.id.logout->{
                val sharedPref = getSharedPreferences("CheckLogin", Context.MODE_PRIVATE) ?: return
                with (sharedPref.edit()) {
                    putString("email","")
                    putString("pass","")
                    apply()
                }
                startActivity(Intent(this,MainActivity::class.java))
            }
            R.id.volunteer ->{
                checkDistressExist()
            }
            R.id.distress ->{
                raiseCall()
            }
            R.id.amenities ->{
                 startActivity(Intent(this,Amenities::class.java))
            }
            R.id.contacts->{
                startActivity(Intent(this,EmergencyContact::class.java))
            }

        }
    }

    private fun checkDistressExist() {
        distressArray.clear()
        mMap.clear()
        mDatabase!!.reference.child("distress").get().addOnSuccessListener {
            if(it.exists()){
            for(i in it.children){
                val lat = i.child("latitude").value.toString()
                val long = i.child("longitude").value.toString()
                distressArray.add(LatLng(lat.toDouble(),long.toDouble()))
            }
            mapCounter = 1
            initialiseMap()
        }}
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
    @SuppressLint("MissingPermission")
    private fun getLocationUpdates()
    {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
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
