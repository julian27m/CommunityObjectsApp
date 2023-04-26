package com.example.navigationdrawercommunityobjects.model;

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.navigationdrawercommunityobjects.R
import com.example.navigationdrawercommunityobjects.view.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.properties.Delegates

class SignUpActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var signupName: EditText
    lateinit var signupUsername: EditText
    private lateinit var signupEmail: EditText
    private lateinit var signupPassword: EditText
    private lateinit var signupButton: Button
    private lateinit var homeButton: Button
    private lateinit var loginRedirectText: TextView
    private lateinit var signupGender: EditText
    private lateinit var signupAge: EditText
    lateinit var database: FirebaseDatabase
    lateinit var reference: DatabaseReference
    private var completeName: Boolean = true
    private var completeUserName: Boolean = true
    private var completeUserNameC : Boolean = true
    private var completeEmail: Boolean = true
    private var completePass: Boolean = true
    private var completeGender: Boolean = true
    private var completeAge: Boolean = true
    private var completeAgeN: Boolean = true
    private var completeAgeC: Boolean = true
    private var fallbackBoolean: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_NavigationDrawerCommunityObjects)
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        setContentView(R.layout.activity_sign_up)

        //Im using this to unable landscape mode
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        signupName = findViewById(R.id.signup_name)

        signupUsername = findViewById(R.id.signup_username)
        signupEmail = findViewById(R.id.signup_email)
        signupPassword = findViewById(R.id.signup_password)
        signupButton = findViewById(R.id.signup_button)
        signupGender = findViewById(R.id.signup_gender)
        signupAge = findViewById(R.id.signup_age)
        homeButton = findViewById(R.id.home_button)



        loginRedirectText = findViewById(R.id.loginRedirectText)
        loginRedirectText.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this@SignUpActivity,
                    LoginActivity::class.java
                )
            )
        })
        homeButton.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this@SignUpActivity,
                    MainActivity::class.java
                )
            )
        })
        signupButton.setOnClickListener(View.OnClickListener {

            database = FirebaseDatabase.getInstance()
            reference = database!!.getReference("users")
            val name = signupName.getText().toString().trim { it <= ' ' }
            val gender = signupGender.getText().toString().trim { it <= ' ' }.lowercase()
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
            //check if the username has '.', '#', '$', '[', or ']' characters
            if (username.contains(".") || username.contains("#") || username.contains("$") || username.contains("[") || username.contains("]")) {
                completeUserNameC = false
                signupUsername.setError("Username cannot contain '.', '#', '$', '[', or ']'")
            } else if (username.isNotEmpty() && !username.contains(".") && !username.contains("#") && !username.contains("$") && !username.contains("[") && !username.contains("]")) {
                completeUserNameC = true
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
            if (age.isEmpty() || age == "") {
                completeAge = false
                signupAge.setError("Age cannot be empty")
            }else if (isNumeric(age)){
                completeAgeN = true
                if (age.toInt() < 16){
                    completeAgeC = false
                    signupAge.setError("Age must be 16 or older")
                }else if (age.toInt() >= 16 && completeAgeN){
                    completeAgeC = true
                }
            }else{
                completeAgeN = false
                signupAge.setError("Age must be a number")
            }



            if (age.length > 2){
                completeAge = false
                signupAge.setError("Age cannot be more than 2 characters")
            }else if (age.length <= 2 && age.isNotEmpty() && completeAgeN) {
                completeAge = true

            }


            val networkConnection = NetworkConnection(applicationContext)
            networkConnection.observe(this, Observer { isConnected ->
                if (isConnected) {
                    if (fallbackBoolean) {
                        //User recovered internet connection
                        showNetworkDialog()
                        fallbackBoolean = false
                        registerUser(user, pass, name, gender, age, username)
                    }else{
                        //User has internet connection fro the start
                        registerUser(user, pass, name, gender, age, username)
                    }
                } else {
                    //User doesn't have internet connection
                    showNetworkDisconnectedDialog()
                    fallbackBoolean = true
                }
            })
    })
    }



    private fun registerUser(user:String, pass:String, name:String, gender:String, age:String, username:String) {
        auth = FirebaseAuth.getInstance()
        if (completeName && completeUserNameC && completeUserName && completeEmail && completePass && completeGender && completeAge && completeAgeN && completeAgeC){
            //TODO: Check if the user is already registered
            val reference = FirebaseDatabase.getInstance().getReference("users")
            val checkUserDatabase = reference.orderByChild("username").equalTo(username)
            checkUserDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        signupUsername.setError("Username already exists")
                    } else {

                        auth.createUserWithEmailAndPassword(user, pass)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val userBuilderClass = UserBuilderClass.Builder()
                                        .setName(name)
                                        .setGender(gender)
                                        .setAge(age)
                                        .setEmail(user)
                                        .setUsername(username)
                                        .setPassword(pass)
                                        .build()
                                    reference.child(username).setValue(userBuilderClass)
                                    Toast.makeText(
                                        this@SignUpActivity,
                                        "SignUp Successful",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    startActivity(
                                        Intent(
                                            this@SignUpActivity,
                                            LoginActivity::class.java
                                        )
                                    )
                                } else {
                                    //signupEmail.setError("Email already exists")
                                    Toast.makeText(
                                        this@SignUpActivity,

                                        "Sign Up Failed" + task.exception!!.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Sign Up Failed" + error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
            }
        }



    fun isNumeric(toCheck: String): Boolean {
        return toCheck.all { char -> char.isDigit() }
    }

    private fun showNetworkDisconnectedDialog() {
        val loginConstraintLayout = findViewById<ConstraintLayout>(R.id.NetworkConstraintLayout)
        val view = LayoutInflater.from(this@SignUpActivity).inflate(R.layout.dialog_network, loginConstraintLayout, false)
        val builder = AlertDialog.Builder(this@SignUpActivity)
        builder.setView(view)
        val alertDialog = builder.create()
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alertDialog.show()

        //dissmiss the dialog after 5 seconds
        val handler = Handler()
        handler.postDelayed({ alertDialog.dismiss() }, 5000)


    }

    private fun showNetworkDialog() {
        val loginConstraintLayout = findViewById<ConstraintLayout>(R.id.NetworkConnectedConstraintLayout)
        val view = LayoutInflater.from(this@SignUpActivity).inflate(R.layout.dialog_network_connected, loginConstraintLayout, false)
        val builder = AlertDialog.Builder(this@SignUpActivity)
        builder.setView(view)
        val alertDialog = builder.create()
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alertDialog.show()

        val handler = Handler()
        handler.postDelayed({ alertDialog.dismiss() }, 5000)

    }
}