package com.example.navigationdrawercommunityobjects.view

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.navigationdrawercommunityobjects.R
import com.example.navigationdrawercommunityobjects.databinding.FragmentDonateBinding
import com.example.navigationdrawercommunityobjects.model.Item
import com.example.navigationdrawercommunityobjects.viewmodel.ItemViewModel

class DonateFragment : Fragment() {

    private val binding: FragmentDonateBinding by lazy {
        FragmentDonateBinding.inflate(layoutInflater)
    }
    private lateinit var viewModel: ItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = binding.root

        viewModel = ViewModelProvider(this).get(ItemViewModel::class.java)

        // Configurar el botón de agregar item
        binding.btnPublish.setOnClickListener {
            val item = Item(
                name = binding.etItemName.text.toString(),
                description = binding.etItemDescription.text.toString(),
                category = binding.spCategory.selectedItem.toString(),
                photo = viewModel.addPhoto(binding.ivItemImage)
            )

            viewModel.addItem(item)
        }

        binding.btnCancel.setOnClickListener {
//            set the home fragment
            val supportFragmentManager = requireActivity().supportFragmentManager
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()
        }

        // Observar el resultado de la operación de agregar item
        viewModel.addItemResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Item agregado correctamente", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Error al agregar item", Toast.LENGTH_SHORT).show()
            }
//            set the home fragment
            val supportFragmentManager = requireActivity().supportFragmentManager
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()
        }


        // Agregar un botón para seleccionar una imagen
        binding.btnAddImage.setOnClickListener {
            // Abrir el fragmento CameraFragment
            val cameraFragment = CameraFragment()
            cameraFragment.setTargetFragment(this, 1)
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, cameraFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }



        return view
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 1) {
            val photoUri = data?.data
            binding.ivItemImage.setImageURI(photoUri)
        }
    }
}
