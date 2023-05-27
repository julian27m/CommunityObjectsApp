package com.example.community_objects.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.community_objects.R
import com.example.community_objects.databinding.FragmentProfileBinding
import com.example.community_objects.databinding.FragmentProfileNotLoggedInBinding
import com.example.community_objects.model.LoginActivity
import com.example.community_objects.model.SignUpActivity
import com.example.community_objects.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding
    private lateinit var bindingNotLoggedIn: FragmentProfileNotLoggedInBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ProfileViewModel.getInstance()
        val user = viewModel.getUser().value
        if (user != null) {
            //Log.d("ProfileFragment", "User is logged in" + user.email)
            binding = FragmentProfileBinding.inflate(inflater, container, false)

            val profileCareer = binding.profileCareer
            val profileEmail = binding.profileEmail
            val profileGender = binding.profileGender
            val titleName = binding.titleName
            val titleUsername = binding.titleUsername
            val profileAge = binding.profileAge
            val donations = binding.donationsNo
            val updates = binding.updatesNo
            val editButton = binding.editProfile

            val viewModel = ProfileViewModel.getInstance()

            viewModel.getUser().observe(viewLifecycleOwner, Observer { user ->
                //Log.d("ProfileFragment", "getUser().observe() called")
                if (user != null) {
                    profileCareer.text = user.career
                    profileEmail.text = user.email
                    profileGender.text = user.gender
                    profileAge.text = user.age.toString()
                    titleName.text = user.name
                    titleUsername.text = user.username
                    donations.text = user.donations.toString()
                    updates.text = user.updates.toString()
                }
            })

            donations.setOnClickListener(View.OnClickListener {
                val fragment = DonationFragment()
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.fragment_container, fragment)
                transaction?.addToBackStack(null)
                transaction?.commit()

            })

            editButton.setOnClickListener(View.OnClickListener {
                val fragment = EditProfileFragment()
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.fragment_container, fragment)
                transaction?.addToBackStack(null)
                transaction?.commit()

            })

            return binding.root

        } else {
            Log.d("ProfileFragment", "User is not logged in" )
            // User is not logged in
            bindingNotLoggedIn = FragmentProfileNotLoggedInBinding.inflate(inflater, container, false)

            val loginButton = bindingNotLoggedIn.loginButton
            val signupRedirectText = bindingNotLoggedIn.signupRedirectText

            loginButton.setOnClickListener {
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
            }
            signupRedirectText.setOnClickListener(View.OnClickListener {
                //start SignUpActivity
                val intent = Intent(activity, SignUpActivity::class.java)
                startActivity(intent)
            })

            return bindingNotLoggedIn.root
        }
    }


}
