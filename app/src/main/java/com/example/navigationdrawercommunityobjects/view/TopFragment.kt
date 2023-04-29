package com.example.navigationdrawercommunityobjects.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.navigationdrawercommunityobjects.databinding.FragmentTopBinding
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
        job = lifecycleScope.launch(Dispatchers.Main) {
            while (true) {
                fetchDataFromRealtimeDatabase()
                delay(5000L)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job?.cancel()
        job = null
    }

    private suspend fun fetchDataFromRealtimeDatabase() {
        withContext(Dispatchers.IO) {
            try {
                val reference = FirebaseDatabase.getInstance().getReference("users")
                val dataSnapshot = reference.orderByChild("donations").limitToLast(3).get().await()
                val data = mutableListOf<String>()
                for (ds in dataSnapshot.children) {
                    val username = ds.child("username").getValue(String::class.java)
                    val donations = ds.child("donations").getValue(String::class.java)
                    data.add("$username:$donations")
                }
                withContext(Dispatchers.Main) {
                    var i = 0
                    for (d in data) {
                        val parts = d.split(":")
                        if (i == 0) {
                            binding.titleUsername2.text = parts[0]
                            binding.donationsNo2.text = parts[1]
                        } else if (i == 1) {
                            binding.titleUsername.text = parts[0]
                            binding.donationsNo.text = parts[1]
                        } else if (i == 2) {
                            binding.titleUsername3.text = parts[0]
                            binding.donationsNo3.text = parts[1]
                        }
                        i++
                    }
                }
                // Store the fetched data in SharedPreferences
                val prefs = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                val editor = prefs.edit()
                editor.putStringSet("top_users", data.toSet())
                editor.apply()
            } catch (e: Exception) {
                Log.d("TopFragment", "Failed to fetch data from database: ${e.message}")

                // If fetch from database fails, try to load from shared preferences
                val prefs = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                val data = prefs.getStringSet("top_users", null)?.toList()
                if (data != null) {
                    withContext(Dispatchers.Main) {
                        var i = 0
                        for (d in data) {
                            val parts = d.split(":")
                            if (i == 0) {
                                binding.titleUsername2.text = parts[0]
                                binding.donationsNo2.text = parts[1]
                            } else if (i == 1) {
                                binding.titleUsername.text = parts[0]
                                binding.donationsNo.text = parts[1]
                            } else if (i == 2) {
                                binding.titleUsername3.text = parts[0]
                                binding.donationsNo3.text = parts[1]
                            }
                            i++
                        }
                    }
                }
            }
        }
    }


}
