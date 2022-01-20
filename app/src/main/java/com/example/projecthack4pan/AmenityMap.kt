package com.example.projecthack4pan

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.projecthack4pan.BuildConfig.MAPS_API_KEY
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.json.JSONObject

import java.lang.ref.WeakReference

class AmenityMap:AppCompatActivity(), OnMapReadyCallback {
    private var mAuth: FirebaseAuth? = null
    private var counter = 0
    private var mapCounter = 0
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null

    private lateinit var distressArray:ArrayList<LatLng>
    private var mMap: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amenity_map)
        distressArray = arrayListOf()
        initialiseMap()
        initialiseFirebase()

//        val task = async.MyAsyncTask(this)
//        task.execute()




        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
    }

    private fun initialiseFirebase(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        checkDistressExist()
    }
    private fun checkDistressExist() {
        distressArray.clear()
        mMap?.clear()
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
    private fun initialiseMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        // Add a marker in Sydney and move the camera
        //distressArray.add(LatLng(37.42342342342342,-122.08395287867832))
        // Toast.makeText(this,"$mapCounter",Toast.LENGTH_SHORT).show()
        if(mapCounter == 1)
        {

            for(i in distressArray) {
                // Toast.makeText(this,"Distress call active",Toast.LENGTH_SHORT).show()
                mMap!!.addMarker(
                    MarkerOptions()
                        .position(i)
                        .title("HELP!!"))
                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(i))
            }

        }
        else if(mapCounter == 0 ) {
            mMap!!.clear()
        }

    }


//    private fun callApi() {
//        queue = Volley.newRequestQueue(this)
//        val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522%2C151.1957362&radius=1500&type=restaurant&keyword=cruise&key=${MAPS_API_KEY}"
//        // Request a string response from the provided URL.
//        val sr = StringRequest(
//            Request.Method.GET, url, Response.Listener { response ->
//            val jsonObj = JSONObject(response)
//            // val data: JSONObject = jsonObj.getJSONObject("AN")
//            val keys: Iterator<*> = jsonObj.keys()
//
//            while (keys.hasNext()) {
//
//                // loop to get the dynamic key
//                val place = keys.next() as String
//
//                val confirmed = jsonObj.getJSONObject(place)
//                    .getString("latitude") as String
//                //cases  updated
//                val deceased = jsonObj.getJSONObject(place)
//                    .getString("longitude") as String
//                //no. of deaths
//                val rec = jsonObj.getJSONObject(place)
//                    .getString("title") as String
//                // recovered patients
//            }
//        },
//            { getString(R.string.apiFailed) })
//
//// Add the request to the RequestQueue.
//        queue.add(sr)
//    }

//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//
//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//    }
}

//private object async {
//    class MyAsyncTask internal constructor(context: AmenityMap) : AsyncTask<Int, String, String?>() {
//
//        private var resp: String? = null
//        private val activityReference: WeakReference<AmenityMap> = WeakReference(context)
//        override fun doInBackground(vararg p0: Int?): String? {
//
//            txt = response
//            return response as String
//        }
//        override fun onPostExecute(result: String?) {
//
//            val activity = activityReference.get()
//            if (activity == null || activity.isFinishing) return
//            activity.t.text = result.let { it }
//
//        }
//    }
//}
