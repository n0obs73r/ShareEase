package com.ogps.shareease

import android.R.attr
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.content.ContentValues.TAG
import android.net.Uri
import android.provider.MediaStore
import android.widget.*
import android.graphics.BitmapFactory
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.File
import java.io.FileNotFoundException


open class SignUpActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    private val storageReference = FirebaseStorage.getInstance().getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        val pfpButton : Button = findViewById(R.id.pfp)
        val signUpButton : Button = findViewById(R.id.signup)
        signUpButton.setOnClickListener {
            signUp()
        }
        pfpButton.setOnClickListener {
            updatepfp()
        }

        val loginButton : Button = findViewById(R.id.login_page_btn)
        loginButton.setOnClickListener {
            finish()
        }
    }

    private fun updatepfp() {

        val Galleryintent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(Galleryintent,1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val pfpView : ImageView = findViewById(R.id.displaypfp)
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1000){
            if (resultCode === RESULT_OK && data!= null) {
                // Get the Uri of data
                val filePath = data.data
                try {
                    val inputStream = contentResolver.openInputStream(filePath!!)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    pfpView!!.setImageBitmap(bitmap)
                    //imageStore(bitmap)
                    uploadImageToFirebase(filePath)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }

        }
    }

    private fun uploadImageToFirebase(imageUri: Uri) {
        val storageRef = storageReference
        storageRef.child("users/${auth.currentUser!!.uid}/profile_picture.jpg")
        storageRef.putFile(imageUri).addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot>(){
            Toast.makeText(this, "Image Uploaded!", Toast.LENGTH_SHORT).show()
        }).addOnFailureListener(){
            Toast.makeText(this, "Image Not Uploaded!", Toast.LENGTH_SHORT).show()
        }
    }



    private fun signUp() {
        val profile = getDetails()
        val userId = profile["email"]


        if (userId != null) {
            db.collection("users").document(userId).set(profile).addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot added")
            }.addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

            val password = findViewById<EditText>(R.id.signup_password).text.toString()

            auth.createUserWithEmailAndPassword(userId, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Registration Failed" + task.exception!!.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun getDetails(): HashMap<String, String> {
        val userId = findViewById<EditText>(R.id.signup_email).text.toString()
        val name = findViewById<EditText>(R.id.name).text.toString()
        val number = findViewById<EditText>(R.id.number).text.toString()
        val address = findViewById<EditText>(R.id.address).text.toString()
        val dob = findViewById<EditText>(R.id.dob).text.toString()

        return hashMapOf(
            "name" to name,
            "number" to number,
            "email" to userId,
            "address" to address,
            "dob" to dob
        )
    }
}