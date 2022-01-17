package com.example.projecthack4pan

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.projecthack4pan.BuildConfig.MAPS_API_KEY

import com.google.android.gms.maps.GoogleMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.json.JSONObject

import java.lang.ref.WeakReference

class AmenityMap:AppCompatActivity() {
    private lateinit var txt:TextView
    private lateinit var queue:RequestQueue
    private lateinit var mMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amenity_map)
        txt = findViewById(R.id.textView)
//        val task = async.MyAsyncTask(this)
//        task.execute()
        callApi()



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
    }

    private fun callApi() {
        queue = Volley.newRequestQueue(this)
        val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522%2C151.1957362&radius=1500&type=restaurant&keyword=cruise&key=${MAPS_API_KEY}"
        // Request a string response from the provided URL.
        val sr = StringRequest(
            Request.Method.GET, url, Response.Listener { response ->
            val jsonObj = JSONObject(response)
            // val data: JSONObject = jsonObj.getJSONObject("AN")
            val keys: Iterator<*> = jsonObj.keys()

            while (keys.hasNext()) {

                // loop to get the dynamic key
                val state = keys.next() as String

                val confirmed = jsonObj.getJSONObject(state).getJSONObject("total")
                    .getString("confirmed") as String
                //cases  updated
                val deceased = jsonObj.getJSONObject(state).getJSONObject("total")
                    .getString("deceased") as String
                //no. of deaths
                val rec = jsonObj.getJSONObject(state).getJSONObject("total")
                    .getString("recovered") as String
                // recovered patients
            }
        },
            { getString(R.string.apiFailed) })

// Add the request to the RequestQueue.
        queue.add(sr)
    }

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
