package com.example.community_objects.view

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.community_objects.R
import com.example.community_objects.databinding.FragmentTopBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SplashActivity : AppCompatActivity() {
    private val SPLASH_SCREEN_TIMEOUT = 3000L // 3 seconds
    private lateinit var binding : FragmentTopBinding

    //This helps to fetch data at lunch time
    //With out this, the information has a delay of 1.4 seconds, legit
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Check if the device has network connection
        if (isNetworkAvailable()) {
            fetchDataFromRealtimeDatabase()
        }


        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, SPLASH_SCREEN_TIMEOUT)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


    private fun fetchDataFromRealtimeDatabase() {
        // Initialize SharedPreferences
        val sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        binding = FragmentTopBinding.inflate(layoutInflater)
        val titleUsername1 = binding.titleUsername
        val donations1 = binding.donationsNo
        val titleUsername2 = binding.titleUsername2
        val donations2 = binding.donationsNo2
        val titleUsername3 = binding.titleUsername3
        val donations3 = binding.donationsNo3
        //val message = binding.message

        val reference = FirebaseDatabase.getInstance().getReference("users")
        reference.orderByChild("donations").limitToLast(3).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = mutableListOf<Pair<String, Int>>()
                for (ds in dataSnapshot.children) {
                    val username = ds.child("username").getValue(String::class.java)
                    val donations = ds.child("donations").getValue(String::class.java)?.toIntOrNull() ?: 0
                    data.add(Pair(username, donations) as Pair<String, Int>)
                }
                data.sortByDescending { it.second }

                // Store data in SharedPreferences
                for ((index, pair) in data.withIndex()) {
                    editor.putString("username$index", pair.first)
                    editor.putInt("donations$index", pair.second)
                }
                editor.apply()
                //message.text = "Congratulations to our top 3 givers of the week!"
                var i = 0
                for (d in data) {
                    if (i == 0) {
                        titleUsername1.text = d.first
                        donations1.text = d.second.toString()
                    } else if (i == 1) {
                        titleUsername2.text = d.first
                        donations2.text = d.second.toString()
                    } else if (i == 2) {
                        titleUsername3.text = d.first
                        donations3.text = d.second.toString()
                    }
                    i++
                }
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                Log.d("TopFragment", "Failed to read value.", error.toException())
            }
        })
    }






}

