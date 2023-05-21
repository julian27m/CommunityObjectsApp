package com.example.community_objects.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.community_objects.model.UserBuilderClass

class ProfileViewModel : ViewModel() {

    val user = MutableLiveData<UserBuilderClass>()
    private val instanceId = Integer.toHexString(hashCode())

    fun setUser(user: UserBuilderClass?) {
        this.user.value = user
        //Log.d("ProfileViewModel", "setUser() called on instance $instanceId")
    }

    fun getUser(): LiveData<UserBuilderClass> {
        return this.user
        //Log.d("ProfileViewModel", "getUser() called on instance $instanceId")
        Log.d("ProfileFragment", user.value.toString())
    }

    // Singleton
    // I made this because there were multiple instances of the ProfileViewModel (I struggled with this for a while)
    // I wanted to make sure that there was only one instance of the ProfileViewModel
    // That instance is called in the ProfileFragment and LoginActivity (update: it is also called in the MainActivity)
    companion object {
        private val instance = ProfileViewModel()

        fun getInstance(): ProfileViewModel {
            return instance
        }
    }
}
