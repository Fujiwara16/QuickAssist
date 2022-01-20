package com.example.projecthack4pan

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SplashScreen : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null

    private var emailTxt: String = ""
    private var passTxt: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val sharedPref = getSharedPreferences("CheckLogin", Context.MODE_PRIVATE)
        emailTxt = sharedPref.getString("email","")!!
        passTxt = sharedPref.getString("pass","")!!

        val backgroundImage: ImageView = findViewById(R.id.SplashScreenImage)
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom)
        backgroundImage.startAnimation(slideAnimation)
        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        check()
        // 3000 is the delayed time in milliseconds.
    }
    private fun check(){
        if (emailTxt!="" && passTxt!="") {

            Log.d(ContentValues.TAG, "Logging in user.")
            emailTxt.let {
                passTxt.let { it1 ->
                    mAuth!!.signInWithEmailAndPassword(it, it1)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with signed-in user's information
                                Log.d(ContentValues.TAG, "signInWithEmail:success")
                                Handler().postDelayed({
                                    val intent = Intent(this, HomePage::class.java)
                                    startActivity(intent)
                                    finish()
                                }, 0)
                            } else {

                                // If sign in fails, display a message to the user.
                                Log.e(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                                Toast.makeText(this, "Authentication failed, Please register.",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        } else {
            Handler().postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 1000)
        }
    }
}