package com.example.navigationdrawercommunityobjects.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.navigationdrawercommunityobjects.R
import com.example.navigationdrawercommunityobjects.databinding.ProductViewBinding
import com.example.navigationdrawercommunityobjects.model.Item
import com.example.navigationdrawercommunityobjects.model.Book
import com.example.navigationdrawercommunityobjects.model.Supplies

class ProductView : FrameLayout {

    private lateinit var binding: ProductViewBinding
    private var width: Int = 0

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    private fun initView() {
//        get screen size for the product image
        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        this.width = (screenWidth - 100) / 2
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.product_view, this, true)

        binding.root.setOnClickListener {
            Toast.makeText(
                 context,
                binding.item!!.category,
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    fun setProduct(item: Item) {
        binding.item = item
        binding.productImage.layoutParams.width = width
//      set the alignment to center

    }
}

