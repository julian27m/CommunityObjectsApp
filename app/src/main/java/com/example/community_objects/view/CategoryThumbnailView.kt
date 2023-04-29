package com.example.community_objects.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.community_objects.R
import com.example.community_objects.databinding.CategoryThumbnailViewBinding
import com.example.community_objects.viewmodel.ProfileViewModel
import com.example.community_objects.viewmodel.VisitViewModel

class CategoryThumbnailView : FrameLayout {
    private lateinit var binding: CategoryThumbnailViewBinding
    private var visitViewModel: VisitViewModel = VisitViewModel()
    private var username: String = ""
    private var userViewModel: ProfileViewModel = ProfileViewModel()

    constructor(context: Context) : super(context) {
        initView(viewLifecycleOwner = context as LifecycleOwner)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(viewLifecycleOwner = context as LifecycleOwner)
    }

    private fun initView(viewLifecycleOwner: LifecycleOwner) {
        binding = CategoryThumbnailViewBinding.inflate(LayoutInflater.from(context), this, true)
        val profileViewModel = ProfileViewModel.getInstance()

        profileViewModel.getUser().observe(viewLifecycleOwner, Observer { user ->
            //Log.d("ProfileFragment", "getUser().observe() called")
            if (user != null) {
                username = user.username.toString()
            }
        })

        binding.root.setOnClickListener {
            println("Category " + binding.categoryName.text + " clicked")
            println("Username: $username")
            visitViewModel.saveVisit(binding.categoryName.text.toString(), username)
        }

    }

    fun setCategory(category: String) {
        binding.categoryName.text = category
        when (category) {
            "EPP" -> binding.categoryImage.setImageResource(R.drawable.epp)
            "Clothes" -> binding.categoryImage.setImageResource(R.drawable.clothes)
            "Books" -> binding.categoryImage.setImageResource(R.drawable.book)
            "Supplies" -> binding.categoryImage.setImageResource(R.drawable.supplies)
            "Other" -> binding.categoryImage.setImageResource(R.drawable.smartphone)
        }

    }

    fun setCategoryImage(image: Unit) {
        binding.categoryImage.setImageResource(R.drawable.ic_launcher_background)
    }

    fun setPopular() {
        binding.lblPopular.visibility = VISIBLE
    }

    fun setCategoryImage(image: String) {
//        set the image from drawables to the image view
        val resId = resources.getIdentifier(image, "drawable", context.packageName)
        binding.categoryImage.setImageResource(resId)
    }
}
