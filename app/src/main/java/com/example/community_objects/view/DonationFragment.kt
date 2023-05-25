package com.example.community_objects.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.community_objects.R
import com.example.community_objects.databinding.FragmentDonationBinding
import com.example.community_objects.databinding.FragmentProfileBinding
import com.example.community_objects.viewmodel.ImageViewModel
import com.example.community_objects.viewmodel.ItemViewModel
import com.example.community_objects.viewmodel.ProfileViewModel
import com.example.community_objects.viewmodel.VisitViewModel
import com.example.community_objects.viewmodel.DonationViewModel

class DonationFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: FragmentDonationBinding

    companion object {
        fun newInstance() = DonationFragment()
    }

    private lateinit var viewModel: DonationViewModel
    private lateinit var itemviewModel: ItemViewModel
    private lateinit var imageViewModel: ImageViewModel
    private lateinit var visitViewModel: VisitViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //return inflater.inflate(R.layout.fragment_donation, container, false)
        profileViewModel = ProfileViewModel.getInstance()
        itemviewModel = ViewModelProvider(this)[ItemViewModel::class.java]
        imageViewModel = ViewModelProvider(this)[ImageViewModel::class.java]
        visitViewModel = ViewModelProvider(this)[VisitViewModel::class.java]
        val user = profileViewModel.getUser().value
        if (user != null) {
            //Log.d("ProfileFragment", "User is logged in" + user.email)
            binding = FragmentDonationBinding.inflate(inflater, container, false)

            val profileName = binding.textName
            val profileUsername = binding.textUsername

            val viewModel = ProfileViewModel.getInstance()

            viewModel.getUser().observe(viewLifecycleOwner, Observer { user ->
                //Log.d("ProfileFragment", "getUser().observe() called")
                if (user != null) {
                    profileName.text = user.name
                    profileUsername.text = user.username
                }
            })
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemviewModel = ViewModelProvider(this)[ItemViewModel::class.java]
        imageViewModel = ViewModelProvider(this)[ImageViewModel::class.java]
        visitViewModel = ViewModelProvider(this)[VisitViewModel::class.java]

        val productsContainer = binding.lytProducts
        val user = profileViewModel.getUser().value
        if (user != null) {
            itemviewModel.getAllItems(user.username) { items ->
                for (item in items) {
                    val productThumbnailView = ProductThumbnailView(requireContext())
                    productThumbnailView.setProduct(item, binding.lytProducts.width)
                    productsContainer.addView(productThumbnailView)
                }
            }
        }
    }


}