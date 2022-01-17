package com.example.projecthack4pan

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null

    private lateinit var cardView:CardView
    private lateinit var name:EditText
    private lateinit var email:EditText
    private lateinit var progressbar:ProgressBar
    private lateinit var phone:EditText
    private lateinit var password:EditText
    private lateinit var back: Button
    private lateinit var signUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        name = findViewById(R.id.nameReg)
        email = findViewById(R.id.emailAddressReg)
        phone = findViewById(R.id.phoneReg)
        password = findViewById(R.id.passwordReg)
        back = findViewById(R.id.backReg)
        signUp = findViewById(R.id.signUpReg)
        progressbar = findViewById(R.id.progressBar)
        cardView = findViewById(R.id.card)
        back.setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
        }
        signUp.setOnClickListener{
            register()
        }
        initialiseFirebase()
    }
    private fun initialiseFirebase(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
    }
    private fun register(){
        val locName = name.text.toString()
        val locEmail = email.text.toString()
        val locPhone = phone.text.toString()
        val locPass = password.text.toString()
        if (locName.isEmpty()) {
            name.error = "Full name is required"
            name.requestFocus()
            return
        }

        if (locEmail.isEmpty()) {
            email.error = "Email is required"
            email.requestFocus()
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(locEmail).matches()) {
            email.error = "Enter a valid email"
            email.requestFocus()
            return
        }//empty(String, String )
        if (locPass.isEmpty()) {
            password.error = "Password is required"
            password.requestFocus()
            return
        }
        if (locPass.length < 6) {
            password.error = "Min. password length should be 6 characters"
            password.requestFocus()
            return
        }
        if (locPhone.isEmpty()) {
            phone.error = "Phone number is required"
            phone.requestFocus()
            return
        }
        if (locPhone.length!=10) {
            phone.error = "Phone number should be equal to 10"
            phone.requestFocus()
            return
        }
//        val user = hashMapOf<String,String>(
//            "Name" to locName,
//            "Email" to locEmail,
//            "PhoneNumber" to locPhone
//        )

        mDatabaseReference = mDatabase!!.reference.child("Users")
        progressbar.visibility = View.VISIBLE
        cardView.visibility = View.GONE
        mAuth!!
            .createUserWithEmailAndPassword(locEmail, locPass)
            .addOnCompleteListener(this) { task ->

                progressbar.visibility = View.INVISIBLE
                if (task.isSuccessful) {

                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    val userId = mAuth!!.currentUser!!.uid
                    val currentUserDb = mDatabaseReference!!.child(userId)
                    //Verify Email
                    verifyEmail()
                    //update user info in firebase
//                    db.collection("users").document(locEmail)
//                        .set(user)
//                        .addOnSuccessListener { documentReference ->
////                                    currentUserDb.child("firestoreId").setValue(documentReference.id)
////                                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//                        }
//                        .addOnFailureListener { e ->
//                            Log.w(ContentValues.TAG, "Error adding document", e)
//                        }
                    //update user profile realtime firebase database

                    currentUserDb.child("Name").setValue(locName)
                    currentUserDb.child("Email").setValue(locEmail)
                    currentUserDb.child("PhoneNumber").setValue(locPhone)
                    currentUserDb.child("Password").setValue(locPass)



                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        this@Register, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }



    }
    private fun verifyEmail() {
        val mUser = mAuth!!.currentUser
        mUser!!.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@Register,
                        "Verification email sent to " + mUser.email,
                        Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(ContentValues.TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(this@Register,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        startActivity(Intent(this,MainActivity::class.java))
    }

}