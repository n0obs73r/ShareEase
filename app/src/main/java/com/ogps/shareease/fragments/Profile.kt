package com.ogps.shareease.fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ogps.shareease.HomePage
import com.ogps.shareease.MainActivity
import com.ogps.shareease.R
import com.ogps.shareease.SignUpActivity

private const val USER_ID = "UserID"

class Profile : Fragment()  {

    private val db = Firebase.firestore

    private var profile: HashMap<String, String> = HashMap()
    private var userID: String? = null

    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userID = it.getString(USER_ID)
        }
        getProfile()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView =  inflater.inflate(R.layout.fragment_profile, container, false)
        val logout : Button = rootView.findViewById(R.id.logout)
        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            logout()
        }

        return rootView

    }

    private fun logout(){
        val intent = Intent(requireActivity().application, MainActivity::class.java)
        startActivity(intent)
    }

    private fun getProfile() {
        db.collection("users").document(userID.toString()).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    profile["name"] = document["name"] as String
                    profile["number"] = document["number"] as String
                    profile["email"] = document["email"] as String
                    profile["address"] = document["address"] as String
                    profile["dob"] = document["dob"] as String
                    setupProfile()
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "Failed with: ", e)
        }
    }

    private fun setupProfile() {
        rootView.findViewById<TextView>(R.id.name).text = profile["name"]
        rootView.findViewById<TextView>(R.id.dob).text = profile["dob"]
        rootView.findViewById<TextView>(R.id.address).text = profile["address"]
        rootView.findViewById<TextView>(R.id.number).text = profile["number"]
        rootView.findViewById<TextView>(R.id.email).text = profile["email"]
    }

    companion object {
        fun newInstance(userID: String) =
            Profile().apply {
                arguments = Bundle().apply {
                    putString(USER_ID, userID)
                }
            }
    }
}