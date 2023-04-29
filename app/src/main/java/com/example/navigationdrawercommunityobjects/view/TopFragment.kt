package com.example.navigationdrawercommunityobjects.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.navigationdrawercommunityobjects.R
import com.example.navigationdrawercommunityobjects.databinding.FragmentProfileBinding
import com.example.navigationdrawercommunityobjects.databinding.FragmentProfileNotLoggedInBinding
import com.example.navigationdrawercommunityobjects.databinding.FragmentTopBinding
import com.example.navigationdrawercommunityobjects.model.LoginActivity
import com.example.navigationdrawercommunityobjects.model.SignUpActivity
import com.example.navigationdrawercommunityobjects.viewmodel.ProfileViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class TopFragment : Fragment() {

    private lateinit var binding: FragmentTopBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTopBinding.inflate(inflater, container, false)

            val titleUsername1 = binding.titleUsername
            val donations1 = binding.donationsNo
            val titleUsername2 = binding.titleUsername2
            val donations2 = binding.donationsNo2
            val titleUsername3 = binding.titleUsername3
            val donations3 = binding.donationsNo3

            val reference = FirebaseDatabase.getInstance().getReference("users")
            //check the user Realtime Database to search for the three users with the most donations
            reference.orderByChild("donations").limitToLast(3).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var i = 0
                    for (ds in dataSnapshot.children) {
                        if (i == 0) {
                            titleUsername3.text = ds.child("username").getValue(String::class.java)
                            donations3.text =
                                ds.child("donations").getValue(String::class.java)
                        } else if (i == 1) {
                            titleUsername2.text = ds.child("username").getValue(String::class.java)
                            donations2.text =
                                ds.child("donations").getValue(String::class.java)
                        } else if (i == 2) {
                            titleUsername1.text = ds.child("username").getValue(String::class.java)
                            donations1.text =
                                ds.child("donations").getValue(String::class.java)
                        }
                        i++
                    }
                }

                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                    Log.d("TopFragment", "Failed to read value.", error.toException())
                }
            })

            return binding.root
        }

}