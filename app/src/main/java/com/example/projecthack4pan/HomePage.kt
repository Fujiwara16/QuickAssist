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

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat


class HomePage : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {
    private lateinit var mMap: GoogleMap

    private lateinit var btnVolunteer:Button
    private lateinit var btnAmenities:Button
    private lateinit var btnDistress:Button
    private lateinit var btnAlert:Button
    private lateinit var userId:String
    private var mAuth: FirebaseAuth? = null
    var counter = 0
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        btnAmenities = findViewById(R.id.amenities)
        btnDistress = findViewById(R.id.distress)
        btnAlert = findViewById(R.id.alert)
        btnVolunteer = findViewById(R.id.volunteer)
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
            val db = mDatabase!!.reference.child("distress")
            db.child(userId).setValue("1")
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
    private fun getLoc() {

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
