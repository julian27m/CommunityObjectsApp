package com.example.community_objects.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.community_objects.databinding.FragmentProductDetailBinding
import com.example.community_objects.viewmodel.ImageViewModel
import com.example.community_objects.viewmodel.ItemViewModel

// this class recieves a ProductDetailView and sets it as the view for the fragment
class ProductDetailFragment(view: ProductDetailView) : Fragment() {

    private lateinit var viewModel: ItemViewModel
    private lateinit var imageViewModel: ImageViewModel
    private val binding: FragmentProductDetailBinding by lazy {
        FragmentProductDetailBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = binding.root
//        get the view from the constructor
        val productDetailView = view as ProductDetailView
//        set the view
        val container: LinearLayout = binding.lytDetail
        binding.lytDetail.addView(productDetailView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ItemViewModel::class.java]
        imageViewModel = ViewModelProvider(this)[ImageViewModel::class.java]
    }

}