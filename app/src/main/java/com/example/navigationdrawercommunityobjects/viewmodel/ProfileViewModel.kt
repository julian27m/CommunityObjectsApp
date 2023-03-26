package com.example.navigationdrawercommunityobjects.viewmodel

import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel() {
    var nameUser: String? = null
    var emailUser: String? = null
    var usernameUser: String? = null
    // This is where you would put your code to retrieve and store user information
}