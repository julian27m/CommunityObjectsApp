package com.example.community_objects.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.community_objects.R
import com.example.community_objects.databinding.RequestThumbnailViewBinding
import com.example.community_objects.model.BookRequest
import com.example.community_objects.model.ItemRequest
import com.example.community_objects.model.SuppliesRequest

class RequestThumbnailView : FrameLayout {
    private var width: Int = 0
    private lateinit var binding: RequestThumbnailViewBinding

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    private fun initView() {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.request_thumbnail_view,
            this,
            true
        )

        binding.root.setOnClickListener {
            Toast.makeText(
                context,
                binding.itemRequest!!.category,
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    fun setRequest(itemRequest: Any, width: Int) {
        if (itemRequest is ItemRequest) {
            binding.itemRequest = itemRequest
        } else if (itemRequest is SuppliesRequest) {
            binding.itemRequest =
                ItemRequest(itemRequest.title, itemRequest.category, itemRequest.description, itemRequest.user)
        } else if (itemRequest is BookRequest) {
            binding.itemRequest =
                ItemRequest(itemRequest.title, itemRequest.category, itemRequest.description, itemRequest.user)
        }
    }
}
