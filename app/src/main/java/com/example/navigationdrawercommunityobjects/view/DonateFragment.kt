package com.example.navigationdrawercommunityobjects.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.example.navigationdrawercommunityobjects.R
import com.example.navigationdrawercommunityobjects.databinding.FragmentDonateBinding
import com.example.navigationdrawercommunityobjects.viewmodel.ItemViewModel
import com.example.navigationdrawercommunityobjects.viewmodel.ProfileViewModel

class DonateFragment : Fragment() {

    private var imageUri: Uri? = null
    private var user: String? = null
    private val binding: FragmentDonateBinding by lazy {
        FragmentDonateBinding.inflate(layoutInflater)
    }
    private lateinit var itemViewModel: ItemViewModel
    private lateinit var username: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = binding.root

        itemViewModel = ViewModelProvider(this)[ItemViewModel::class.java]

        val profileViewModel = ProfileViewModel.getInstance()

        profileViewModel.getUser().observe(viewLifecycleOwner, Observer { user ->
            //Log.d("ProfileFragment", "getUser().observe() called")
            if (user != null) {
                username = user.username.toString()
            }
        })

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
            print("User set")
            item["user"] = username

            //            println("Item: $item")
            //            println("ImageUri: $imageUri")
            if (imageUri != null) {
                itemViewModel.addItem(item, imageUri!!) { success ->
//                    println("intenta publicar")
                    if (success) {
                        Toast.makeText(
                            requireContext(),
                            "Item added successfully",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Error adding the item",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    //            set the home fragment
                    val supportFragmentManager = requireActivity().supportFragmentManager
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment()).commit()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please select an image first",
                    Toast.LENGTH_SHORT
                ).show()
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

        binding.spCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (binding.spCategory.selectedItem.toString()) {
                    "Clothes" -> {
                        //                        let color and size be visible
                        binding.etItemColors.visibility = View.VISIBLE
                        binding.etItemSize.visibility = View.VISIBLE
                        binding.lblColor.visibility = View.VISIBLE
                        binding.lblSize.visibility = View.VISIBLE

                        binding.etItemDegree.visibility = View.GONE
                        binding.etItemType.visibility = View.GONE
                        binding.etItemAuthor.visibility = View.GONE
                        binding.etItemSubject.visibility = View.GONE
                        binding.etItemReference.visibility = View.GONE
                        binding.lblDegree.visibility = View.GONE
                        binding.lblType.visibility = View.GONE
                        binding.lblAuthor.visibility = View.GONE
                        binding.lblSubject.visibility = View.GONE
                        binding.lblReference.visibility = View.GONE

                    }
                    "Books" -> {
                        //                        let author and subject be visible
                        binding.etItemAuthor.visibility = View.VISIBLE
                        binding.etItemSubject.visibility = View.VISIBLE
                        binding.lblAuthor.visibility = View.VISIBLE
                        binding.lblSubject.visibility = View.VISIBLE

                        binding.etItemDegree.visibility = View.GONE
                        binding.etItemType.visibility = View.GONE
                        binding.etItemColors.visibility = View.GONE
                        binding.etItemSize.visibility = View.GONE
                        binding.etItemReference.visibility = View.GONE
                        binding.lblDegree.visibility = View.GONE
                        binding.lblType.visibility = View.GONE
                        binding.lblColor.visibility = View.GONE
                        binding.lblSize.visibility = View.GONE
                        binding.lblReference.visibility = View.GONE
                    }
                    "Protective equipment" -> {
                        //                        let type and degree be visible
                        binding.etItemDegree.visibility = View.VISIBLE
                        binding.etItemType.visibility = View.VISIBLE
                        binding.lblDegree.visibility = View.VISIBLE
                        binding.lblType.visibility = View.VISIBLE

                        binding.etItemAuthor.visibility = View.GONE
                        binding.etItemSubject.visibility = View.GONE
                        binding.etItemColors.visibility = View.GONE
                        binding.etItemSize.visibility = View.GONE
                        binding.etItemReference.visibility = View.GONE
                        binding.lblAuthor.visibility = View.GONE
                        binding.lblSubject.visibility = View.GONE
                        binding.lblColor.visibility = View.GONE
                        binding.lblSize.visibility = View.GONE
                        binding.lblReference.visibility = View.GONE
                    }
                    "School and University Supplies" -> {
                        //                        let reference be visible
                        binding.etItemReference.visibility = View.VISIBLE
                        binding.lblReference.visibility = View.VISIBLE

                        binding.etItemDegree.visibility = View.GONE
                        binding.etItemType.visibility = View.GONE
                        binding.etItemAuthor.visibility = View.GONE
                        binding.etItemSubject.visibility = View.GONE
                        binding.etItemColors.visibility = View.GONE
                        binding.etItemSize.visibility = View.GONE
                        binding.lblDegree.visibility = View.GONE
                        binding.lblType.visibility = View.GONE
                        binding.lblAuthor.visibility = View.GONE
                        binding.lblSubject.visibility = View.GONE
                        binding.lblColor.visibility = View.GONE
                        binding.lblSize.visibility = View.GONE

                    }
                    "Other" -> {
                        //                        let none be visible
                        binding.etItemDegree.visibility = View.GONE
                        binding.etItemType.visibility = View.GONE
                        binding.etItemAuthor.visibility = View.GONE
                        binding.etItemSubject.visibility = View.GONE
                        binding.etItemColors.visibility = View.GONE
                        binding.etItemSize.visibility = View.GONE
                        binding.etItemReference.visibility = View.GONE
                        binding.lblDegree.visibility = View.GONE
                        binding.lblType.visibility = View.GONE
                        binding.lblAuthor.visibility = View.GONE
                        binding.lblSubject.visibility = View.GONE
                        binding.lblColor.visibility = View.GONE
                        binding.lblSize.visibility = View.GONE
                        binding.lblReference.visibility = View.GONE
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //                set the first element
                binding.spCategory.setSelection(0)
            }
        }

        //        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        //            if (result.resultCode == RESULT_OK) {
        //                val _photoUri: Uri? = result.data?.data
        ////                println("photoUri: $_photoUri")
        //                imageUri = _photoUri
        ////                println("imageUri: $imageUri")
        //                binding.ivItemImage.setImageURI(_photoUri)
        //            }
        //        }

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
