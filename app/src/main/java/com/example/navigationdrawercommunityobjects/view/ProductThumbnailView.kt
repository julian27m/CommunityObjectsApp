package com.example.navigationdrawercommunityobjects.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.navigationdrawercommunityobjects.R
import com.example.navigationdrawercommunityobjects.databinding.ProductThumbnailViewBinding
import com.example.navigationdrawercommunityobjects.model.Book
import com.example.navigationdrawercommunityobjects.model.Item
import com.example.navigationdrawercommunityobjects.model.Supplies

class ProductThumbnailView : FrameLayout {

    private lateinit var binding: ProductThumbnailViewBinding
    private var width: Int = 0

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    private fun initView() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.product_thumbnail_view, this, true)

        binding.root.setOnClickListener {
            Toast.makeText(
                 context,
                binding.item!!.category,
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    fun setProduct(item: Any , width: Int) {
        if (item is Item) {
            binding.item = item
        } else if (item is Supplies) {
            binding.item = Item(item.title, item.category, item.description, item.imageURL, item.user)
        } else if (item is Book) {
            binding.item = Item(item.title, item.category, item.description, item.imageURL, item.user)
        }

        binding.productImage.layoutParams.width = (width - 100) / 2
    }
}

