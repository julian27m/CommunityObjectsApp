package com.example.navigationdrawercommunityobjects.model

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.navigationdrawercommunityobjects.R
import com.example.navigationdrawercommunityobjects.viewmodel.ProfileViewModel

class ProfileActivity : AppCompatActivity() {
    lateinit var profileName: TextView
    lateinit var profileEmail: TextView
    lateinit var profileUsername: TextView
    lateinit var titleName: TextView
    lateinit var titleUsername: TextView
    private lateinit var viewModel: ProfileViewModel

    //Button editProfile;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_profile)

        profileName = findViewById(R.id.profileName)
        profileEmail = findViewById(R.id.profileEmail)
        profileUsername = findViewById(R.id.profileUsername)
        titleName = findViewById(R.id.titleName)
        titleUsername = findViewById(R.id.titleUsername)
        //editProfile = findViewById(R.id.editButton);
        showAllUserData()
    }

    fun showAllUserData() {
        val intent = intent
        val nameUser = intent.getStringExtra("name")
        val emailUser = intent.getStringExtra("email")
        val usernameUser = intent.getStringExtra("username")
        titleName!!.text = nameUser
        titleUsername!!.text = usernameUser
        profileName!!.text = nameUser
        profileEmail!!.text = emailUser
        profileUsername!!.text = usernameUser
    }
}