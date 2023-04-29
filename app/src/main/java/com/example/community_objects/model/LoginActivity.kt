package com.example.community_objects.model;

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.community_objects.R
import com.example.community_objects.databinding.ActivityLoginBinding
import com.example.community_objects.view.MainActivity
import com.example.community_objects.view.showNetworkDialog
import com.example.community_objects.view.showNetworkDisconnectedDialog
import com.example.community_objects.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.properties.Delegates

class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private var fallbackBoolean by Delegates.notNull<Boolean>()


    val viewModel = ProfileViewModel.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Im using this to unable landscape mode
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        auth = FirebaseAuth.getInstance()


        fallbackBoolean = false

        binding.loginButton.setOnClickListener(View.OnClickListener {

            if (!validateUsername() or !validatePassword()) {
            } else {
                val networkConnection = NetworkConnection(applicationContext)
                networkConnection.observe(this, Observer { isConnected ->
                    if (isConnected) {
                        if (fallbackBoolean) {
                            showNetworkDialog()
                            fallbackBoolean = false
                            checkUser()
                        }else{
                            checkUser()
                        }
                    } else {
                        showNetworkDisconnectedDialog()
                        fallbackBoolean = true
                    }
                })
            }
        })


        binding.signUpRedirectText.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        })
        binding.homeButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        })


        binding.forgotPassword.setOnClickListener(View.OnClickListener {
            val builder = AlertDialog.Builder(this@LoginActivity)
            val dialogView = layoutInflater.inflate(R.layout.dialog_forgot, null)
            val emailBox = dialogView.findViewById<EditText>(R.id.emailBox)
            builder.setView(dialogView)
            val dialog = builder.create()
            dialogView.findViewById<View>(R.id.btnReset).setOnClickListener(View.OnClickListener {
                val userEmail = emailBox.text.toString()
                if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail)
                        .matches()
                ) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Enter your registered email id",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@OnClickListener
                }
                auth.sendPasswordResetEmail(userEmail).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@LoginActivity, "Check your email", Toast.LENGTH_SHORT)
                            .show()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Unable to send, failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
            dialogView.findViewById<View>(R.id.btnCancel).setOnClickListener { dialog.dismiss() }
            if (dialog.window != null) {
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()
        })
    }

    fun validateUsername(): Boolean {
        val `val` = binding.loginUsername.text.toString()
        return if (`val`.isEmpty()) {
            binding.loginUsername.error = "Username cannot be empty"
            false
        } else {
            binding.loginUsername.error = null
            true
        }
    }

    fun validatePassword(): Boolean {
        val `val` = binding.loginPassword.text.toString()
        return if (`val`.isEmpty()) {
            binding.loginPassword.error = "Password cannot be empty"
            false
        } else {
            binding.loginPassword.error = null
            true
        }
    }

    fun checkUser() {
        val userUsername = binding.loginUsername.text.toString().trim { it <= ' ' }
        val userPassword = binding.loginPassword.text.toString().trim { it <= ' ' }
        val reference = FirebaseDatabase.getInstance().getReference("users")
        val checkUserDatabase = reference.orderByChild("username").equalTo(userUsername)
        checkUserDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    binding.loginUsername.error = null
                    val passwordFromDB = snapshot.child(userUsername).child("password").getValue(
                        String::class.java
                    )
                    if (passwordFromDB == userPassword) {
                        binding.loginUsername.error = null
                        val nameFromDB = snapshot.child(userUsername).child("name").getValue(
                            String::class.java
                        )
                        val emailFromDB = snapshot.child(userUsername).child("email").getValue(
                            String::class.java
                        )
                        val usernameFromDB =
                            snapshot.child(userUsername).child("username").getValue(
                                String::class.java
                            )
                        val ageFromDB =
                            snapshot.child(userUsername).child("age").getValue(
                                String::class.java
                            )
                        val genderFromDB =
                            snapshot.child(userUsername).child("gender").getValue(
                                String::class.java
                            )
                        val donationsFromDB =
                            snapshot.child(userUsername).child("donations").getValue(
                                String::class.java
                            )
                        val careerFromDB =
                            snapshot.child(userUsername).child("career").getValue(
                                String::class.java
                            )

                        val userBuilder = UserBuilderClass.Builder()
                            .setName(nameFromDB.toString())
                            .setEmail(emailFromDB.toString())
                            .setUsername(usernameFromDB.toString())
                            .setPassword(passwordFromDB.toString())
                            .setGender(genderFromDB.toString())
                            .setAge(ageFromDB.toString())
                            .setDonations(donationsFromDB.toString())
                            .setCareer(careerFromDB.toString())
                            .build()

                        viewModel.setUser(userBuilder)

                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))

                        //Toast.makeText(applicationContext, "Welcome ${nameFromDB}!", Toast.LENGTH_LONG).show()

                    } else {
                        binding.loginPassword.error = "Invalid Credentials"
                        binding.loginPassword.requestFocus()
                    }
                } else {
                    binding.loginUsername.error = "User does not exist"
                    binding.loginUsername.requestFocus()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

}