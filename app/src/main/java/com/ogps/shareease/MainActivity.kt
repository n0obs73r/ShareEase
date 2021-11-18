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

        val signUpButton : Button = findViewById(R.id.signupbutton)
        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
    private fun login(){
        val user = findViewById<EditText>(R.id.email).text.toString()
        val password = findViewById<EditText>(R.id.password).text.toString()

        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(user, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Logged In", Toast.LENGTH_LONG).show()
                val intent = Intent(this, HomePage::class.java)
                intent.putExtra("USER_ID", user)
                startActivity(intent)
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