package com.example.navigationdrawercommunityobjects.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.content.Intent
import com.example.navigationdrawercommunityobjects.model.ProfileActivity
import com.example.navigationdrawercommunityobjects.R
import com.example.navigationdrawercommunityobjects.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)

        val profileName: TextView = root.findViewById(R.id.profileName)
        val profileEmail: TextView = root.findViewById(R.id.profileEmail)
        val profileUsername: TextView = root.findViewById(R.id.profileUsername)

        profileName.text = viewModel.nameUser
        profileEmail.text = viewModel.emailUser
        profileUsername.text = viewModel.usernameUser

        //invoke profile activity
        val profileIntent = Intent(activity, ProfileActivity::class.java)
        profileIntent.putExtra("name", viewModel.nameUser)
        profileIntent.putExtra("email", viewModel.emailUser)
        profileIntent.putExtra("username", viewModel.usernameUser)
        startActivity(profileIntent)
        

        return root
    }
}