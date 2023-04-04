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
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var signupName: EditText
    lateinit var signupUsername: EditText
    private lateinit var signupEmail: EditText
    private lateinit var signupPassword: EditText
    private lateinit var signupButton: Button
    private lateinit var loginRedirectText: TextView
    private lateinit var signupGender: EditText
    private lateinit var signupAge: EditText
    lateinit var database: FirebaseDatabase
    lateinit var reference: DatabaseReference
    private var completeName: Boolean = true
    private var completeUserName: Boolean = true
    private var completeEmail: Boolean = true
    private var completePass: Boolean = true
    private var completeGender: Boolean = true
    private var completeAge: Boolean = true
    private var completeAgeN: Boolean = true
    private var completeAgeC: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_NavigationDrawerCommunityObjects)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()
        signupName = findViewById(R.id.signup_name)

        signupUsername = findViewById(R.id.signup_username)
        signupEmail = findViewById(R.id.signup_email)
        signupPassword = findViewById(R.id.signup_password)
        signupButton = findViewById(R.id.signup_button)
        signupGender = findViewById(R.id.signup_gender)
        signupAge = findViewById(R.id.signup_age)

        loginRedirectText = findViewById(R.id.loginRedirectText)
        signupButton.setOnClickListener(View.OnClickListener {
            database = FirebaseDatabase.getInstance()
            reference = database!!.getReference("users")
            val name = signupName.getText().toString().trim { it <= ' ' }
            val gender = signupGender.getText().toString().trim { it <= ' ' }
            val age = signupAge.getText().toString().trim { it <= ' ' }
            val username = signupUsername.getText().toString().trim { it <= ' ' }
            val user = signupEmail.getText().toString().trim { it <= ' ' }
            val pass = signupPassword.getText().toString().trim { it <= ' ' }



            if (user.isEmpty()) {
                completeEmail = false
                signupEmail.setError("Email cannot be empty")

            }
            if (!user.endsWith("uniandes.edu.co") && user.isNotEmpty()) {
                completeEmail = false
                signupEmail.setError("Email must be a uniandes email")
            } else if (user.endsWith("uniandes.edu.co")) {
                completeEmail = true
            }
            if (username.isEmpty()) {
                completeUserName = false
                signupUsername.setError("Username cannot be empty")
            }

            if (username.length > 30) {
                completeUserName = false
                signupUsername.setError("Username cannot be more than 30 characters")
            } else if (username.length <= 30 && username.isNotEmpty()) {
                completeUserName = true
            }
            if (name.isEmpty()) {
                completeName = false
                signupName.setError("Name cannot be empty")
            }
            if (name.length > 30) {
                completeName = false
                signupName.setError("Name cannot be more than 30 characters")
            } else if (username.length <= 30 && username.isNotEmpty()) {
                completeName = true
            }
            if (pass.isEmpty()) {
                completePass = false
                signupPassword.setError("Password cannot be empty")
            } else if (pass.isNotEmpty()) {
                completePass = true
            }
            if (gender.isEmpty()){
                completeGender = false
                signupGender.setError("Gender cannot be empty")
            }
            if (gender.length >30){
                completeGender = false
                signupGender.setError("Gender cannot be more than 30 characters")
            }else if (gender.length <= 30 && gender.isNotEmpty())
            {
                completeGender = true
            }
           if (age.isEmpty()) {
               completeAge = false
               signupAge.setError("Age cannot be empty")
           }
           //check if age.int throws an exception
           try {
                age.toInt()
              } catch (e: NumberFormatException) {
                completeAgeN = false
                signupAge.setError("Age must be a number")
           }

           if (age.length > 2){
               completeAge = false
               signupAge.setError("Age cannot be more than 2 characters")
              }else if (age.length <= 2 && age.isNotEmpty() && completeAgeN) {
               completeAge = true

           }
           if (age.toInt() < 16){
               completeAgeC = false
               signupAge.setError("Age must be 16 or older")
              }else if (age.toInt() >= 16 && completeAgeN){
               completeAgeC = true
              }

            if (completeName && completeUserName && completeEmail && completePass && completeGender && completeAge && completeAgeN && completeAgeC) {

                auth!!.createUserWithEmailAndPassword(user, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userBuilderClass = UserBuilderClass(name, gender, age, user, username, pass)
                        reference!!.child(username).setValue(userBuilderClass)
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