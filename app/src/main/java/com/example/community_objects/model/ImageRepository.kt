package com.example.community_objects.model

import android.graphics.Bitmap
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ImageRepository() {
    private val serviceAdapter = FirebaseServiceAdapter()
    suspend fun getImage(imageRef: StorageReference, fragment: Fragment): Bitmap {
        val url = serviceAdapter.getImageUrl(imageRef)
        return withContext(Dispatchers.IO) {
            Glide.with(fragment)
                .asBitmap()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .submit()
                .get()
            }
    }

    suspend fun getCategoryImage(fragment: Fragment, url: String): Bitmap {
        return withContext(Dispatchers.IO) {
            Glide.with(fragment)
                .asBitmap()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .submit()
                .get()
        }
    }
}
