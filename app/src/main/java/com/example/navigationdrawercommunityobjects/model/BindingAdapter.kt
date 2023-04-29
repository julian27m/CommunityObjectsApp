package com.example.navigationdrawercommunityobjects.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.navigationdrawercommunityobjects.R

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String?) {
    Glide.with(imageView.context)
        .load(R.drawable.ic_download_image)
        .into(imageView)
    if (!url.isNullOrEmpty()) {
        try {
        Glide.with(imageView.context)
            .load(url)
            .into(imageView)
        } catch (e: Exception) {
            Glide.with(imageView.context)
                .load(R.drawable.ic_broken_image)
                .into(imageView)
        }
    } else {
        Glide.with(imageView.context)
            .load(R.drawable.ic_broken_image)
            .into(imageView)
    }
}
