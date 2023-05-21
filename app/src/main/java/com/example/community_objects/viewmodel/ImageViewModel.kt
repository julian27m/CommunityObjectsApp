package com.example.community_objects.viewmodel

import android.graphics.Bitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.community_objects.model.ImageRepository
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch

class ImageViewModel() : ViewModel() {
    private val imageRepository = ImageRepository()
    private val _image = MutableLiveData<Bitmap>()
    val image: LiveData<Bitmap>
        get() = _image

    fun loadImage(imageRef: StorageReference, fragment: Fragment) {
        viewModelScope.launch {
            val bitmap = imageRepository.getImage(imageRef, fragment)
            _image.value = bitmap
        }
    }

//    fun getCategoryImage(fragment: Fragment, category: String) {
//        var url = ""
//        when (category) {
//            "EPP" -> { url = "https://firebasestorage.googleapis.com/v0/b/community-auth-e564a.appspot.com/o/resources%2FEPP.png?alt=media&token=13b6bcfd-1543-48ef-a17c-2f55cef849c9" }
//            "Books" -> { url = "https://firebasestorage.googleapis.com/v0/b/community-auth-e564a.appspot.com/o/resources%2Fbook.png?alt=media&token=61733b45-c5f3-414a-b822-69136f41d90e" }
//            "Clothes" -> { url = "https://firebasestorage.googleapis.com/v0/b/community-auth-e564a.appspot.com/o/resources%2Fclothes.png?alt=media&token=2d509d1d-3a7f-4bea-96b7-7afa54d20a7e" }
//            "Supplies" -> { url = "https://firebasestorage.googleapis.com/v0/b/community-auth-e564a.appspot.com/o/resources%2Fsupplies.png?alt=media&token=84901585-ce4d-4059-b92a-3490e93079bc" }
//            "Other" -> { url = "https://firebasestorage.googleapis.com/v0/b/community-auth-e564a.appspot.com/o/resources%2Fsmartphone.png?alt=media&token=0d33c73e-a2f2-4506-8b44-1377f65b6bb8" }
//        }
//        viewModelScope.launch {
//            val bitmap = imageRepository.getCategoryImage(fragment, url)
//            _image.value = bitmap
//        }
//    }

    fun getCategoryImageSimple(fragment: Fragment, url: String): String {
//        temporary function that returns the image in the drawable folder
//        epp -> epp.png
//        books -> book.png
//        clothes -> clothes.png
//        supplies -> supplies.png
//        other -> smartphone.png
        when (url) {
            "EPP" -> { return "epp.png" }
            "Books" -> { return "book.png" }
            "Clothes" -> { return "clothes.png" }
            "Supplies" -> { return "supplies.png" }
            "Other" -> { return "smartphone.png" }
        }
        return ""
    }
}
