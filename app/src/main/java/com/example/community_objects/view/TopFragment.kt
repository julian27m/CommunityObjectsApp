package com.example.community_objects.view

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.community_objects.databinding.FragmentTopBinding
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class TopFragment : Fragment() {
    private lateinit var binding: FragmentTopBinding
    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isNetworkAvailable()) {
            job = lifecycleScope.launch(Dispatchers.Main) {
                while (true) {
                    fetchDataFromRealtimeDatabase()
                    delay(5000L)
                }
            }
        } else {
            loadCachedData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job?.cancel()
        job = null
    }

    private fun loadCachedData() {
        val sharedPreferences = requireActivity().getSharedPreferences("userData", Context.MODE_PRIVATE)

        val data = mutableListOf<Pair<String, Int>>()
        for (i in 0..2) {
            val username = sharedPreferences.getString("username$i", "")
            val donations = sharedPreferences.getInt("donations$i", 0)
            if (username != null && username.isNotEmpty()) {
                data.add(Pair(username, donations))
            }
        }

        var i = 0
        for (d in data) {
            if (i == 0) {
                binding.titleUsername.text = d.first
                binding.donationsNo.text = d.second.toString()
            } else if (i == 1) {
                binding.titleUsername2.text = d.first
                binding.donationsNo2.text = d.second.toString()
            } else if (i == 2) {
                binding.titleUsername3.text = d.first
                binding.donationsNo3.text = d.second.toString()
            }
            i++
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private suspend fun fetchDataFromRealtimeDatabase() {
        // Initialize SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("userData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        withContext(Dispatchers.IO) {
            //Here is where I fetch the data from the database
            try {
                val reference = FirebaseDatabase.getInstance().getReference("users")
                val dataSnapshot = reference.orderByChild("donations").limitToLast(3).get().await()
                val data = mutableListOf<Pair<String, Int>>()
                for (ds in dataSnapshot.children) {
                    val username = ds.child("username").getValue(String::class.java)
                    val donations = ds.child("donations").getValue(String::class.java)?.toIntOrNull() ?: 0
                    data.add(Pair(username, donations) as Pair<String, Int>)
                }
                data.sortByDescending { it.second }

                //Store data in SharedPreferences
                for ((index, pair) in data.withIndex()) {
                    editor.putString("username$index", pair.first)
                    editor.putInt("donations$index", pair.second)
                }
                editor.apply()

                data.sortByDescending { it.second }
                withContext(Dispatchers.Main) {
                    //This is where I update the UI
                    var i = 0
                    for (d in data) {
                        if (i == 0) {
                            binding.titleUsername.text = d.first
                            binding.donationsNo.text = d.second.toString()
                        } else if (i == 1) {
                            binding.titleUsername2.text = d.first
                            binding.donationsNo2.text = d.second.toString()
                        } else if (i == 2) {
                            binding.titleUsername3.text = d.first
                            binding.donationsNo3.text = d.second.toString()
                        }
                        i++
                    }
                }
            } catch (e: Exception) {
                // Handle error
                Log.d("TopFragment", "Failed to fetch data from database: ${e.message}")

                // If fetch from database fails, try to load from SharedPreferences
                withContext(Dispatchers.Main) {
                    loadCachedData()
                }
            }
        }
    }


}
