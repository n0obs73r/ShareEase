package com.ogps.shareease

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button : Button = findViewById(R.id.loginbutton)
        button.setOnClickListener {
            login()
        }
        val SignUpButton : Button = findViewById(R.id.signupbutton)
        SignUpButton.setOnClickListener {
            val intent2 = Intent(this, SignUpActivity::class.java)
            startActivity(intent2)
        }

    }
    private fun login(){
        var user = findViewById<EditText>(R.id.email).text.toString()
        var password = findViewById<EditText>(R.id.password).text.toString()

        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(user, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Logged In", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    this,
                    "" + task.exception!!.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}