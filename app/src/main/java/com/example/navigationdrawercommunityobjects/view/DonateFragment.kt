package com.example.navigationdrawercommunityobjects.view

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import com.example.navigationdrawercommunityobjects.R
import com.example.navigationdrawercommunityobjects.databinding.FragmentDonateBinding
import com.example.navigationdrawercommunityobjects.model.Item
import com.example.navigationdrawercommunityobjects.viewmodel.ItemViewModel

class DonateFragment : Fragment() {

    private var imageUri: Uri? = null
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

        viewModel = ViewModelProvider(this)[ItemViewModel::class.java]

        // Configurar el botón de agregar item
        binding.btnPublish.setOnClickListener {
            val item = HashMap<String, String>()
            item["name"] = binding.etItemName.text.toString()
            item["description"] = binding.etItemDescription.text.toString()
            item["category"] = binding.spCategory.selectedItem.toString()
            item["degree"] = binding.etItemDegree.text.toString()
            item["type"] = binding.etItemType.text.toString()
            item["author"] = binding.etItemAuthor.text.toString()
            item["subject"] = binding.etItemSubject.text.toString()
            item["colors"] = binding.etItemColors.text.toString()
            item["size"] = binding.etItemSize.text.toString()
            item["reference"] = binding.etItemReference.text.toString()

//            println("Item: $item")
//            println("ImageUri: $imageUri")
            if (imageUri != null) {
                viewModel.addItem(item, imageUri!!) { success ->
//                    println("intenta publicar")
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
            } else {
                Toast.makeText(requireContext(), "Por favor seleccione una imagen", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCancel.setOnClickListener {
//            set the home fragment
            val supportFragmentManager = requireActivity().supportFragmentManager
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commit()
        }

//        // Observar el resultado de la operación de agregar item
//        viewModel.addItemResult.observe(viewLifecycleOwner) { success ->
//            if (success) {
//                Toast.makeText(requireContext(), "Item agregado correctamente", Toast.LENGTH_SHORT)
//                    .show()
//            } else {
//                Toast.makeText(requireContext(), "Error al agregar item", Toast.LENGTH_SHORT).show()
//            }
////            set the home fragment
//            val supportFragmentManager = requireActivity().supportFragmentManager
//            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()
//        }

        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val _photoUri: Uri? = result.data?.data
//                println("photoUri: $_photoUri")
                imageUri = _photoUri
//                println("imageUri: $imageUri")
                binding.ivItemImage.setImageURI(_photoUri)
            }
        }

        // Agregar un botón para seleccionar una imagen
        binding.btnAddImage.setOnClickListener {
            //Start CameraActivity.kt
            val intent = Intent(requireContext(), CameraActivity::class.java)
            startActivityForResult(intent, 1)

        }

        binding.btnPickImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }



        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        println("onActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResultonActivityResult")
//        println(requestCode)
//        println(resultCode)
//        println(data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
//            println("Funcionanananannananannnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn")
            val photoUri: Uri? = data?.data
//            print(photoUri)
            imageUri = photoUri
            binding.ivItemImage.setImageURI(photoUri)
        }
    }
}