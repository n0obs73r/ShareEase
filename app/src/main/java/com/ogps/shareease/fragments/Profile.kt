package com.ogps.shareease.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ogps.shareease.MainActivity
import com.ogps.shareease.R
import android.graphics.BitmapFactory
import android.widget.ImageView
import com.google.firebase.storage.FirebaseStorage

private const val USER_ID = "UserID"

class Profile : Fragment()  {

    private val db = Firebase.firestore
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference
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
                    profile["carName"] = document["carName"] as String
                    profile["carNumber"] = document["carNumber"] as String
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
        val storageRef = storageReference.child("users/$userID/profile.jpg")
        val imageView = rootView.findViewById<ImageView>(R.id.profilepicture)
        val onemb = (1024 * 1024).toLong()
        storageRef.getBytes(onemb)
            .addOnSuccessListener { bytes ->
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                imageView.setImageBitmap(bmp)
            }.addOnFailureListener {
                //Toast.makeText(this, "No Such file or Path found!!", Toast.LENGTH_LONG).show()
            }

        rootView.findViewById<TextView>(R.id.name).text = profile["name"]
        rootView.findViewById<TextView>(R.id.dob).text = profile["dob"]
        rootView.findViewById<TextView>(R.id.address).text = profile["address"]
        rootView.findViewById<TextView>(R.id.number).text = profile["number"]
        rootView.findViewById<TextView>(R.id.email).text = profile["email"]
        rootView.findViewById<TextView>(R.id.carname).text = profile["carName"]
        rootView.findViewById<TextView>(R.id.carnumber).text = profile["carNumber"]
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