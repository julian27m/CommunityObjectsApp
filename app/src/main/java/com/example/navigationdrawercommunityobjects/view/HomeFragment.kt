package com.example.navigationdrawercommunityobjects.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.navigationdrawercommunityobjects.R
import com.example.navigationdrawercommunityobjects.model.NetworkConnection
import com.example.navigationdrawercommunityobjects.databinding.FragmentHomeBinding
import com.example.navigationdrawercommunityobjects.viewmodel.HomeViewModel
import com.example.navigationdrawercommunityobjects.viewmodel.ItemViewModel

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: ItemViewModel
    private val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = binding.root
        viewModel = ViewModelProvider(this)[ItemViewModel::class.java]
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ItemViewModel::class.java]

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

        val productsContainer = binding.lytProducts

//        get all the products
//        viewModel.getAllItems { items ->
//            for (item in items) {
//                println("item: $item")
//                val productView = ProductView(requireContext())
//                productView.setProduct(item)
//                productsContainer.addView(productView)
//            }
//        }

        viewModel.items.observe(viewLifecycleOwner, Observer { items ->
            for (item in items) {
                println("item: $item")
                val productView = ProductView(requireContext())
                productView.setProduct(item)
                productsContainer.addView(productView)
            }
        })

    }

}