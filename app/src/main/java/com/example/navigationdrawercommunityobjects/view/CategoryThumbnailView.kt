package com.example.navigationdrawercommunityobjects.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.navigationdrawercommunityobjects.R
import com.example.navigationdrawercommunityobjects.databinding.CategoryThumbnailViewBinding
import com.example.navigationdrawercommunityobjects.viewmodel.ProfileViewModel
import com.example.navigationdrawercommunityobjects.viewmodel.VisitViewModel

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