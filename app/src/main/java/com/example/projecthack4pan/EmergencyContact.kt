package com.example.projecthack4pan

import android.Manifest.permission.READ_CONTACTS
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat.startActivityForResult

import android.provider.ContactsContract.CommonDataKinds.Phone

import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone.*
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.jetbrains.annotations.Contract
import androidx.core.app.ActivityCompat.startActivityForResult





class EmergencyContact : AppCompatActivity(), View.OnClickListener {
    private val REQUEST_CODE = 1
    lateinit var contactsList: ListView
    private lateinit var contactUri:Uri
    private lateinit var nameArray:ArrayList<String>
    private lateinit var phoneArray:ArrayList<String>
    private lateinit var btnAdd:Button
    private val cursorAdapter: SimpleCursorAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_contact)
        nameArray = arrayListOf()
        phoneArray = arrayListOf()
        btnAdd = findViewById(R.id.add)
        btnAdd.setOnClickListener(this)
        contactsList = findViewById(R.id.list)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
            contactUri = data!!.data!!
        getData()
    }

    @SuppressLint("Range", "Recycle")
    private fun getData() {
        val columns = arrayOf(DISPLAY_NAME, HAS_PHONE_NUMBER)

//        val cursor = contentResolver.query(contactUri,projection,null,null,null)
        val contentResolver = contentResolver
        val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, columns, null, null, null)

        if (cursor != null && cursor.count > 0)
        {

            if (cursor.moveToFirst()) {

                    val name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME))
                    val phone = cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER))
                       phoneArray.add(phone)
                        nameArray.add(name)
                        val myListAdapter = MyListAdapter(this, nameArray, phoneArray)
                        contactsList.adapter = myListAdapter
                cursor.close()
//                if (cursor!=null && cursor.count > 0) {
//                    // Toast.makeText(this, "check$", Toast.LENGTH_SHORT).show()
//                    if (cursor.moveToFirst()) {
//
//                        Toast.makeText(this, "check$phone", Toast.LENGTH_SHORT).show()
//
//                    }
//                }
            }
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.add->{

                val result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                //Toast.makeText(this, "$result", Toast.LENGTH_SHORT).show()
                if (result == PackageManager.PERMISSION_GRANTED){
                    //Toast.makeText(this, "check$", Toast.LENGTH_SHORT).show()
                    val uri: Uri = Uri.parse("content://contacts")
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = CONTENT_TYPE
                    startActivityForResult(intent,REQUEST_CODE)
                } else {
                       // Toast.makeText(this, "check$", Toast.LENGTH_SHORT).show()
                        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CONTACTS), 1)
                    }
                // Permission has already been granted



            }
        }
    }}


