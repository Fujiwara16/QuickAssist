package com.example.projecthack4pan

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MyListAdapter(private val context: Activity, private val name: ArrayList<String>, private val phone: ArrayList<String>)
    : ArrayAdapter<String>(context, R.layout.contacts_list_item, name) {
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.contacts_list_item, null, true)
        val phoneLoc = rowView.findViewById<TextView>(R.id.phone)
        val nameLoc = rowView.findViewById<TextView>(R.id.name)
        nameLoc.text = name[position]
        phoneLoc.text = phone[position]
        return rowView
    }
}