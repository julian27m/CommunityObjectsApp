package com.example.navigationdrawercommunityobjects.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.navigationdrawercommunityobjects.R
import com.example.navigationdrawercommunityobjects.model.LoginActivity
import com.example.navigationdrawercommunityobjects.model.SignUpActivity
import com.example.navigationdrawercommunityobjects.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var profileUsername: TextView
    private lateinit var titleName: TextView
    private lateinit var titleUsername: TextView
    private lateinit var signupRedirectText: TextView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ProfileViewModel.getInstance()
        val user = viewModel.getUser().value
        if (user != null) {
            //Log.d("ProfileFragment", "User is logged in" + user.email)
            val view = inflater.inflate(R.layout.fragment_profile, container, false)


            profileName = view.findViewById(R.id.profileName)
            profileEmail = view.findViewById(R.id.profileEmail)
            profileUsername = view.findViewById(R.id.profileUsername)
            titleName = view.findViewById(R.id.titleName)
            titleUsername = view.findViewById(R.id.titleUsername)

            val viewModel = ProfileViewModel.getInstance()

            viewModel.getUser().observe(viewLifecycleOwner, Observer { user ->
                //Log.d("ProfileFragment", "getUser().observe() called")
                if (user != null) {
                    profileName.text = user.name
                    profileEmail.text = user.email
                    profileUsername.text = user.username
                    titleName.text = user.name
                    titleUsername.text = user.username
                }
            })


            return view


        } else {
            Log.d("ProfileFragment", "User is not logged in" )
            // User is not logged in
            val view = inflater.inflate(R.layout.fragment_profile_not_logged_in, container, false)
            val loginButton = view.findViewById<Button>(R.id.login_button)
            signupRedirectText = view.findViewById(R.id.signupRedirectText)
            loginButton.setOnClickListener {
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
            }
            signupRedirectText.setOnClickListener(View.OnClickListener {
                //start SignUpActivity
                val intent = Intent(activity, SignUpActivity::class.java)
                startActivity(intent)
            })
            return view
        }
    }
}