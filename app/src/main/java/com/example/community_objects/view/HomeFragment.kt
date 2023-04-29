package com.example.community_objects.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.community_objects.databinding.FragmentHomeBinding
import com.example.community_objects.model.NetworkConnection
import com.example.community_objects.viewmodel.ImageViewModel
import com.example.community_objects.viewmodel.ItemViewModel
import com.example.community_objects.viewmodel.ProfileViewModel
import com.example.community_objects.viewmodel.VisitViewModel

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: ItemViewModel
    private lateinit var imageViewModel: ImageViewModel
    private lateinit var visitViewModel: VisitViewModel
    private val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = binding.root
        viewModel = ViewModelProvider(this)[ItemViewModel::class.java]
        imageViewModel = ViewModelProvider(this)[ImageViewModel::class.java]
        visitViewModel = ViewModelProvider(this)[VisitViewModel::class.java]
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ItemViewModel::class.java]
        imageViewModel = ViewModelProvider(this)[ImageViewModel::class.java]
        visitViewModel = ViewModelProvider(this)[VisitViewModel::class.java]
        if (!isAdded) {
            return
        }
//        get the greeting label
        val greetingLabel = binding.lblGreeting
//        set the text based on time
        val currentHour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        if (currentHour in 0..11) {
            greetingLabel.text = "Good morning!"
        } else if (currentHour in 12..17) {
            greetingLabel.text = "Good afternoon!"
        } else if (currentHour in 18..20) {
            greetingLabel.text = "Good evening!"
        } else {
            greetingLabel.text = "Good night!"
        }

        var fallbackBoolean = false

        val networkConnection = NetworkConnection(requireContext())
        networkConnection.observe(viewLifecycleOwner, Observer { isConnected ->
            if (isConnected) {
                if (fallbackBoolean) {
                    //Log.d("MainActivity", "NetworkConnection: isConnected")
                    //dismissNetworkDialog()
                    fallbackBoolean = false
                    binding.lblNoInternet.visibility = View.GONE
                }
                //Log.d("MainActivity", "NetworkConnection: isConnected")
                //showNetworkDialog()
            } else {
                //Log.d("MainActivity", "NetworkConnection: isNotConnected")
                fallbackBoolean = true
//                return to the home fragment
                binding.lblNoInternet.visibility = View.VISIBLE
            }
        })



        val profileViewModel = ProfileViewModel.getInstance()

        profileViewModel.getUser().observe(viewLifecycleOwner, Observer { user ->
            //Log.d("ProfileFragment", "getUser().observe() called")
            if (user != null) {
                username = user.username.toString()
            }
        })

        val productsContainer = binding.lytProducts


        val categories = listOf("EPP", "Books", "Clothes", "Supplies", "Other")
//        get the category popularity from VisitviewModel

        var popularity: Map<String, Int>
        popularity = mapOf(
            "EPP" to 0,
            "Books" to 0,
            "Clothes" to 0,
            "Supplies" to 0,
            "Other" to 0
        )

        visitViewModel.visits.observe(viewLifecycleOwner, Observer { visits ->
            val categoryPopularity =
                visits.groupBy { it.category }.mapValues { it.value.size }.toMutableMap()
            println("categoryPopularity: $categoryPopularity")
            if (categoryPopularity != {}) {
                for (category in categories) {
                    if (categoryPopularity.containsKey(category)) {
                        popularity = popularity.plus(
                            category to categoryPopularity[category]!!
                        )
                    }
                }
//                    add categoryPopularity "" to popularity "Other"
                if (categoryPopularity.containsKey("")) {
                    popularity = popularity.plus(
                        "Other" to categoryPopularity[""]!!
                    )
                }
                //Log.d("HomeFragment", "popularity: $popularity")
                println("popularity: $popularity")
//                println("popularity is not nullllllllllllllllllllllllllllllllllllllllllllllllllll")
//                save only the keys for the categories sorted by popularity
                val sortedPopularity =
                    popularity.toList().sortedByDescending { (_, value) -> value }
                        .toMap().keys
                println("sortedPopularity: $sortedPopularity")
                var i = 1
//           add a category_thumbnail_view with the categories in order in binding.cat1 to binding.cat5
                for (category in sortedPopularity) {
                    val categoryThumbnailView = CategoryThumbnailView(requireContext())
                    categoryThumbnailView.setCategory(category)
                    when (i) {
                        1 -> {
                            if (!fallbackBoolean){
                            categoryThumbnailView.setPopular()
                            }
                            binding.cat1.addView(categoryThumbnailView)
                        }
                        2 -> binding.cat2.addView(categoryThumbnailView)
                        3 -> binding.cat3.addView(categoryThumbnailView)
                        4 -> binding.cat4.addView(categoryThumbnailView)
                        5 -> binding.cat5.addView(categoryThumbnailView)
                    }
                    i++
                }
            }

        })


        viewModel.items.observe(viewLifecycleOwner, Observer
        { items ->
            for (item in items) {
//                println("item: $item")
                val productThumbnailView = ProductThumbnailView(requireContext())
                productThumbnailView.setProduct(item, binding.lytProducts.width)
                productsContainer.addView(productThumbnailView)
            }
        })

    }

}