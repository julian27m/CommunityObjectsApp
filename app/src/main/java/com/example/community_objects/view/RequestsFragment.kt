package com.example.community_objects.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.community_objects.databinding.FragmentRequestsBinding
import com.example.community_objects.model.NetworkConnection
import com.example.community_objects.viewmodel.ItemViewModel
import com.example.community_objects.viewmodel.ProfileViewModel

class RequestsFragment : Fragment() {

    private lateinit var viewModel: ItemViewModel
    private val binding: FragmentRequestsBinding by lazy {
        FragmentRequestsBinding.inflate(layoutInflater)
    }

    private lateinit var username: String

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

        val requestsContainer = binding.lytRequests

        viewModel.getItemsRequest() { items ->
            if (!isAdded) {
                return@getItemsRequest
            }
//            check if the list is empty
            if (items.isNotEmpty()) {
                requestsContainer.removeAllViews()
                for (item in items) {
                    val itemCard = RequestThumbnailView(requireContext())
                    itemCard.setRequest(item, binding.lytRequests.width)
                    requestsContainer.addView(itemCard)
                }
            } else {
                requestsContainer.removeAllViews()
                val noItemsLabel = TextView(requireContext())
                noItemsLabel.text = "No requests"
                requestsContainer.addView(noItemsLabel)
            }

        }

    }


}
