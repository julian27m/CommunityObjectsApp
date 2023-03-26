package com.example.navigationdrawercommunityobjects.view

import android.os.Bundle
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

    private lateinit var viewModel: ItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDonateBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this).get(ItemViewModel::class.java)

        // Configurar el botón de agregar item
        binding.btnPublish.setOnClickListener {
            val item = Item(
                name = binding.etItemName.text.toString(),
                description = binding.etItemDescription.text.toString(),
                categories = binding.spCategory.selectedItem.toString().split(","),
                photos = listOf("url1", "url2", "url3") // Aquí debes agregar la lógica para obtener las imágenes
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

        return view
    }
}
