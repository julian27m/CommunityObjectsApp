package com.example.community_objects.model;

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.community_objects.view.MainActivity
import com.example.community_objects.view.showCareersDialog
import com.example.community_objects.view.showNetworkDialog
import com.example.community_objects.view.showNetworkDisconnectedDialog
import com.example.community_objects.databinding.ActivitySignUpBinding
import com.example.community_objects.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding
    lateinit var database: FirebaseDatabase
    lateinit var reference: DatabaseReference
    private var completeName: Boolean = true
    private var completeUserName: Boolean = true
    private var completeUserNameC: Boolean = true
    private var completeEmail: Boolean = true
    private var completePass: Boolean = true
    private var completeGender: Boolean = true
    private var completeAge: Boolean = true
    private var completeAgeN: Boolean = true
    private var completeAgeC: Boolean = true
    private var fallbackBoolean: Boolean = false
    private var completeCareer: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_CommunityObjects)
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Im using this to unable landscape mode
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        binding.loginRedirectText.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
        }
        binding.homeButton.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
        }
        binding.signupButton.setOnClickListener {

            database = FirebaseDatabase.getInstance()
            reference = database!!.getReference("users")
            val name = binding.signupName.text.toString().trim { it <= ' ' }
            val gender = binding.signupGender.text.toString().trim { it <= ' ' }.lowercase()
            val age = binding.signupAge.text.toString().trim { it <= ' ' }
            val username = binding.signupUsername.text.toString().trim { it <= ' ' }
            val user = binding.signupEmail.text.toString().trim { it <= ' ' }
            val donations = "0"
            val updates = "0"
            val career = binding.signupCareer.text.toString().trim { it <= ' ' }.replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .uppercase()
            //This is for development purposes only, it will be removed in the final version
            val pass = binding.signupPassword.text.toString().trim { it <= ' ' }



            if (user.isEmpty()) {
                completeEmail = false
                binding.signupEmail.error = "Email cannot be empty"

            }
            if (!user.endsWith("uniandes.edu.co") && user.isNotEmpty()) {
                completeEmail = false
                binding.signupEmail.error = "Email must be a uniandes email"
            } else if (user.endsWith("uniandes.edu.co")) {
                completeEmail = true
            }
            if (username.isEmpty()) {
                completeUserName = false
                binding.signupUsername.error = "Username cannot be empty"
            }
            //check if the username has '.', '#', '$', '[', or ']' characters
            if (username.contains(".") || username.contains("#") || username.contains("$") || username.contains(
                    "["
                ) || username.contains("]")
            ) {
                completeUserNameC = false
                binding.signupUsername.error = "Username cannot contain '.', '#', '$', '[', or ']'"
            } else if (username.isNotEmpty() && !username.contains(".") && !username.contains("#") && !username.contains(
                    "$"
                ) && !username.contains("[") && !username.contains("]")
            ) {
                completeUserNameC = true
            }

            if (username.length > 10) {
                completeUserName = false
                binding.signupUsername.error = "Username cannot be more than 10 characters"
            } else if (username.length <= 30 && username.isNotEmpty()) {
                completeUserName = true
            }
            if (name.isEmpty()) {
                completeName = false
                binding.signupName.error = "Name cannot be empty"
            }
            if (name.length > 30) {
                completeName = false
                binding.signupName.error = "Name cannot be more than 30 characters"
            } else if (username.length <= 30 && username.isNotEmpty()) {
                completeName = true
            }
            if (pass.isEmpty()) {
                completePass = false
                binding.signupPassword.error = "Password cannot be empty"
            } else if (pass.isNotEmpty()) {
                completePass = true
            }
            if (gender.isEmpty()) {
                completeGender = false
                binding.signupGender.error = "Gender cannot be empty"
            }
            if (gender.length > 30) {
                completeGender = false
                binding.signupGender.error = "Gender cannot be more than 30 characters"
            } else if (gender.length <= 30 && gender.isNotEmpty()) {
                completeGender = true
            }
            if (age.isEmpty() || age == "") {
                completeAge = false
                binding.signupAge.error = "Age cannot be empty"
            } else if (isNumeric(age)) {
                completeAgeN = true
                if (age.toInt() < 16) {
                    completeAgeC = false
                    binding.signupAge.error = "Age must be 16 or older"
                } else if (age.toInt() >= 16 && completeAgeN) {
                    completeAgeC = true
                }
            } else {
                completeAgeN = false
                binding.signupAge.error = "Age must be a number"
            }



            if (age.length > 2) {
                completeAge = false
                binding.signupAge.error = "Age cannot be more than 2 characters"
            } else if (age.length <= 2 && age.isNotEmpty() && completeAgeN) {
                completeAge = true

            }

            if (career.isEmpty()) {
                completeCareer = false
                binding.signupCareer.error = "Career cannot be empty"
            } else if (isInAndes(career)) {
                completeCareer = true
            } else {
                completeCareer = false
                binding.signupCareer.error = "Check your career name"
                showCareersDialog()
            }

            val networkConnection = NetworkConnection(applicationContext)
            networkConnection.observe(this, Observer { isConnected ->
                if (isConnected) {
                    if (fallbackBoolean) {
                        //User recovered internet connection
                        showNetworkDialog()
                        fallbackBoolean = false
                        registerUser(user, pass, name, gender, age, username, donations, updates, career)
                    } else {
                        //User has internet connection fro the start
                        registerUser(user, pass, name, gender, age, username, donations, updates, career)
                    }
                } else {
                    //User doesn't have internet connection
                    showNetworkDisconnectedDialog()
                    fallbackBoolean = true
                }
            })
        }
    }


    private fun registerUser(
        user: String,
        pass: String,
        name: String,
        gender: String,
        age: String,
        username: String,
        donations: String,
        updates: String,
        career: String
    ) {
        auth = FirebaseAuth.getInstance()
        if (completeName && completeUserNameC && completeUserName && completeEmail && completePass && completeGender && completeAge && completeAgeN && completeAgeC && completeCareer) {
            //TODO: Check if the user is already registered
            val reference = FirebaseDatabase.getInstance().getReference("users")
            val checkUserDatabase = reference.orderByChild("username").equalTo(username)
            checkUserDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        binding.signupUsername.setError("Username already exists")
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
                                        .setDonations(donations)
                                        .setUpdates(updates)
                                        .setCareer(career)
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

    fun isInAndes(toCheck: String): Boolean {
        val names = listOf("ESTUDIOS DIRIGIDOS",
            "ADMINISTRACION",
            "ECONOMIA",
            "GOBIERNO Y ASUNTOS PUBLICOS",
            "BIOLOGIA",
            "FISICA",
            "GEOCIENCIAS",
            "MATEMATICAS",
            "MICROBIOLOGIA",
            "QUIMICA",
            "MEDICINA",
            "ARQUITECTURA",
            "DISEÑO",
            "ARTE",
            "HISTORIA DEL ARTE",
            "LITERATURA",
            "MUSICA",
            "NARRATIVAS DIGITALES",
            "INGENIERIA AMBIENTAL",
            "INGENIERIA DE ALIMENTOS",
            "INGENIERIA BIOMEDICA",
            "INGENIERIA CIVIL",
            "INGENIERIA ELECTRICA",
            "INGENIERIA ELECTRONICA",
            "INGENIERIA INDUSTRIAL",
            "INGENIERIA MECANICA",
            "INGENIERIA QUIMICA",
            "INGENIERIA DE SISTEMAS Y COMPUTACION",
            "DERECHO",
            "ANTROPOLOGIA",
            "CIENCIA POLITICA",
            "ESTUDIOS GLOBALES",
            "FILOSOFIA",
            "HISTORIA",
            "LENGUAS Y CULTURA",
            "PSICOLOGIA",
            "LICENCIATURA EN ARTES",
            "LICENCIATURA EN BIOLOGIA",
            "LICENCIATURA EN EDUCACION INFANTIL",
            "LICENCIATURA EN ESPAÑOL Y FILOLOGIA",
            "LICENCIATURA EN FILOSOFIA",
            "LICENCIATURA EN FISICA",
            "LICENCIATURA EN HISTORIA",
            "LICENCIATURA EN MATEMATICAS",
            "LICENCIATURA EN QUIMICA")
        return names.contains(toCheck)
    }
}