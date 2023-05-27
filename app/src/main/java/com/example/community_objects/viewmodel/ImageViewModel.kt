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
