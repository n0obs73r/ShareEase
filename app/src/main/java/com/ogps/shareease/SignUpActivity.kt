package com.ogps.shareease

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance();

//        if (auth.getCurrentUser() != null) {
//            val intent = Intent(this, MainActivity::class.java);
//            startActivity(intent);
//            finish();
//        }

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
        val user = findViewById<EditText>(R.id.signup_email).text.toString()
        var password = findViewById<EditText>(R.id.signup_password).text.toString()

        auth = FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword(user, password).addOnCompleteListener(this, OnCompleteListener{ task ->
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