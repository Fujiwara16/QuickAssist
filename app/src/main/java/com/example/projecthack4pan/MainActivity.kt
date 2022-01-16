package com.example.projecthack4pan

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var emailTxt: EditText
    private lateinit var passTxt: EditText
    private lateinit var loginBtn: Button
    private lateinit var registerBtn: Button
    private lateinit var progressbar: ProgressBar

    private var mAuth: FirebaseAuth? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        emailTxt = findViewById(R.id.emailAddress)
        passTxt = findViewById(R.id.password)
        registerBtn = findViewById(R.id.register)
        loginBtn = findViewById(R.id.login)
        progressbar = findViewById(R.id.progressBar)
        val sharedPref = getSharedPreferences("CheckLogin",Context.MODE_PRIVATE)
        emailTxt.setText(sharedPref.getString("email",""))
        passTxt.setText(sharedPref.getString("pass",""))
        initialiseFirebase()

        if(emailTxt.text.toString() != "" && passTxt.text.toString() != "")
        {
            check()
        }
        registerBtn.setOnClickListener(this)
        loginBtn.setOnClickListener(this)


    }
    override fun onClick(view: View?) {
        when (view?.id){
            R.id.login ->{
                check()
            }
            R.id.register->{
                startActivity(Intent(this, Register::class.java))
            }

        }
    }
    private fun initialiseFirebase(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
    }
    private fun check(){
        val emailAd = emailTxt.text.toString()
        val password = passTxt.text.toString()

        val sharedPref = getSharedPreferences("CheckLogin", Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString("email",emailAd)
            putString("pass",password)
            apply()
        }

        if (!TextUtils.isEmpty(emailAd) && !TextUtils.isEmpty(password)) {
            progressbar.visibility = View.VISIBLE
            Log.d(ContentValues.TAG, "Logging in user.")
            mAuth!!.signInWithEmailAndPassword(emailAd, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with signed-in user's information
                        Log.d(ContentValues.TAG, "signInWithEmail:success")
                        getName()
                    } else {
                        progressbar.visibility = View.INVISIBLE
                        // If sign in fails, display a message to the user.
                        Log.e(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this@MainActivity, "Authentication failed, Please register.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
        }
    }
    private fun getName(){
        var name = ""
        val userId = mAuth!!.currentUser!!.uid
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mDatabaseReference!!.child(userId).get().addOnSuccessListener {
            if(it.exists())
            {

                val nm = it.child("Name").value
                name=nm.toString()
            }
            else
            {
                Toast.makeText(this,"User not found!!", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener{
            Toast.makeText(this,"failed !!", Toast.LENGTH_SHORT).show()
        }
        val intent =  Intent(this@MainActivity, HomePage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("name",name)
        startActivity(intent)
    }
}