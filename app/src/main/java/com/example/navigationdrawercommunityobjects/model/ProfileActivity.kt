package com.example.navigationdrawercommunityobjects.model

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.navigationdrawercommunityobjects.databinding.FragmentProfileBinding
import com.example.navigationdrawercommunityobjects.viewmodel.ProfileViewModel

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel

    //Button editProfile;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Im using this to unable landscape mode
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        showAllUserData()
    }

    private fun showAllUserData() {
        val intent = intent
        val nameUser = intent.getStringExtra("name")
        val emailUser = intent.getStringExtra("email")
        val usernameUser = intent.getStringExtra("username")
        binding.titleName.text = nameUser
        binding.titleUsername.text = usernameUser
        binding.profileName.text = nameUser
        binding.profileEmail.text = emailUser
        binding.profileUsername.text = usernameUser
    }
}
