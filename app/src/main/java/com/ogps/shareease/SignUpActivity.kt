package com.ogps.shareease

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.content.ContentValues.TAG


open class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    val userId = findViewById<EditText>(R.id.signup_email).text.toString()
    val name = findViewById<EditText>(R.id.name).text.toString()
    val number = findViewById<EditText>(R.id.number).text.toString()
    val address = findViewById<EditText>(R.id.address).text.toString()
    val dob = findViewById<EditText>(R.id.dob).text.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance();

        val signUpButton : Button = findViewById(R.id.signup)
        signUpButton.setOnClickListener {
            signUp()
        }
        val loginButton : Button = findViewById(R.id.login_page_btn)
        loginButton.setOnClickListener {
            val intent2 = Intent(this, MainActivity::class.java)
            startActivity(intent2)
        }
    }

    private fun signUp() {

        val profile = hashMapOf(
            "name" to name,
            "number" to number,
            "email" to userId,
            "address" to address,
            "dob" to dob
        )
        db.collection("users")
            .document(userId)
            .set(profile)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added ")
            }
                //with ID: ${documentReference.id}
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
        var password = findViewById<EditText>(R.id.signup_password).text.toString()

        auth = FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword(userId, password).addOnCompleteListener(this, OnCompleteListener{ task ->
            if(task.isSuccessful) {
                Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Registration Failed"+ task.exception!!.message, Toast.LENGTH_LONG).show()
            }
        })

    }
    }