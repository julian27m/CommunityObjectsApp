package com.example.navigationdrawercommunityobjects.model;

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.navigationdrawercommunityobjects.R
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var signupEmail: EditText
    private lateinit var signupPassword: EditText
    private lateinit var signupButton: Button
    private lateinit var loginRedirectText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()
        signupEmail = findViewById(R.id.signup_email)
        signupPassword = findViewById(R.id.signup_password)
        signupButton = findViewById(R.id.signup_button)
        loginRedirectText = findViewById(R.id.loginRedirectText)
        signupButton.setOnClickListener(View.OnClickListener {
            val user = signupEmail.getText().toString().trim { it <= ' ' }
            val pass = signupPassword.getText().toString().trim { it <= ' ' }
            if (user.isEmpty()) {
                signupEmail.setError("Email cannot be empty")
            }
            if (pass.isEmpty()) {
                signupPassword.setError("Password cannot be empty")
            } else {
                auth!!.createUserWithEmailAndPassword(user, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@SignUpActivity, "SignUp Successful", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                    } else {
                        Toast.makeText(
                            this@SignUpActivity,
                            "SignUp Failed" + task.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
        loginRedirectText.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this@SignUpActivity,
                    LoginActivity::class.java
                )
            )
        })
    }
}