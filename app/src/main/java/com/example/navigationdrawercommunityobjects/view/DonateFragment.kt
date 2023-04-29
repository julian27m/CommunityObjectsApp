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
import com.example.navigationdrawercommunityobjects.model.UserBuilderClass
import com.example.navigationdrawercommunityobjects.viewmodel.ItemViewModel
import com.example.navigationdrawercommunityobjects.viewmodel.ProfileViewModel
import com.google.firebase.database.*

class DonateFragment : Fragment() {

    private var imageUri: Uri? = null
    private var user: String? = null
    private val binding: FragmentDonateBinding by lazy {
        FragmentDonateBinding.inflate(layoutInflater)
    }
    private lateinit var itemViewModel: ItemViewModel
    private lateinit var username: String

    lateinit var database: FirebaseDatabase
    lateinit var reference: DatabaseReference

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

        val reference = FirebaseDatabase.getInstance().getReference("users")



        // Configurar el botón de agregar item
        binding.btnPublish.setOnClickListener {
//            check the min length of the visible Text
            if (binding.etItemName.text.length < 3 && binding.etItemName.visibility == View.VISIBLE) {
                binding.etItemName.error = "Name must be at least 3 characters long"
                return@setOnClickListener
            }
            if (binding.etItemDescription.text.length < 20 && binding.etItemDescription.visibility == View.VISIBLE) {
                binding.etItemDescription.error = "Description must be at least 20 characters long"
                return@setOnClickListener
            }
            if (binding.etItemDegree.text.length < 3 && binding.etItemDegree.visibility == View.VISIBLE) {
                binding.etItemDegree.error = "Degree must be at least 3 characters long"
                return@setOnClickListener
            }
            if (binding.etItemType.text.length < 3 && binding.etItemType.visibility == View.VISIBLE) {
                binding.etItemType.error = "Type must be at least 3 characters long"
                return@setOnClickListener
            }
            if (binding.etItemAuthor.text.isEmpty() && binding.etItemAuthor.visibility == View.VISIBLE) {
                binding.etItemAuthor.error = "Author must not be empty"
                return@setOnClickListener
            }
            if (binding.etItemSubject.text.length < 3 && binding.etItemSubject.visibility == View.VISIBLE) {
                binding.etItemSubject.error = "Subject must be at least 3 characters long"
                return@setOnClickListener
            }
            if (binding.etItemColors.text.length < 3 && binding.etItemColors.visibility == View.VISIBLE) {
                binding.etItemColors.error = "Colors must be at least 3 characters long"
                return@setOnClickListener
            }
            if (binding.etItemSize.text.isEmpty() && binding.etItemSize.visibility == View.VISIBLE) {
                binding.etItemSize.error = "Size can't be empty"
                return@setOnClickListener
            }
            if (binding.etItemReference.text.length < 3 && binding.etItemReference.visibility == View.VISIBLE) {
                binding.etItemReference.error = "Reference must be at least 3 characters long"
                return@setOnClickListener
            }

//          check that any of the visible text input has errors
            if (binding.etItemName.error != null && binding.etItemName.visibility == View.VISIBLE) {
                return@setOnClickListener
            } else if (binding.etItemDescription.error != null && binding.etItemDescription.visibility == View.VISIBLE) {
                return@setOnClickListener
            } else if (binding.etItemDegree.error != null && binding.etItemDegree.visibility == View.VISIBLE) {
                return@setOnClickListener
            } else if (binding.etItemType.error != null && binding.etItemType.visibility == View.VISIBLE) {
                return@setOnClickListener
            } else if (binding.etItemAuthor.error != null && binding.etItemAuthor.visibility == View.VISIBLE) {
                return@setOnClickListener
            } else if (binding.etItemSubject.error != null && binding.etItemSubject.visibility == View.VISIBLE) {
                return@setOnClickListener
            } else if (binding.etItemColors.error != null && binding.etItemColors.visibility == View.VISIBLE) {
                return@setOnClickListener
            } else if (binding.etItemSize.error != null && binding.etItemSize.visibility == View.VISIBLE) {
                return@setOnClickListener
            } else if (binding.etItemReference.error != null && binding.etItemReference.visibility == View.VISIBLE) {
                return@setOnClickListener
            }


//            disable the buttons
            binding.btnPublish.isEnabled = false
            binding.btnCancel.isEnabled = false

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
//            print("User set")
            item["user"] = username
            item["title"] = binding.etItemName.text.toString()

            //            println("Item: $item")
            //            println("ImageUri: $imageUri")
            if (imageUri != null) {
                itemViewModel.addItem(item, imageUri!!) { success ->
//                    println("intenta publicar")
                    if (success) {
                        val checkUserDatabase = reference.orderByChild("username").equalTo(username)
                        checkUserDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    val nameFromDB = snapshot.child(username).child("name").getValue(
                                        String::class.java
                                    )
                                    val passwordFromDB = snapshot.child(username).child("password").getValue(
                                        String::class.java
                                    )
                                    val emailFromDB = snapshot.child(username).child("email").getValue(
                                        String::class.java
                                    )
                                    val usernameFromDB =
                                        snapshot.child(username).child("username").getValue(
                                            String::class.java
                                        )
                                    val ageFromDB =
                                        snapshot.child(username).child("age").getValue(
                                            String::class.java
                                        )
                                    val genderFromDB =
                                        snapshot.child(username).child("gender").getValue(
                                            String::class.java
                                        )
                                    val donationsFromDB =
                                        snapshot.child(username).child("donations").getValue(
                                            String::class.java
                                        )
                                    val donationsNum = donationsFromDB.toString().toInt() + 1

                                    val userBuilder = UserBuilderClass.Builder()
                                        .setName(nameFromDB.toString())
                                        .setEmail(emailFromDB.toString())
                                        .setUsername(usernameFromDB.toString())
                                        .setPassword(passwordFromDB.toString())
                                        .setGender(genderFromDB.toString())
                                        .setAge(ageFromDB.toString())
                                        .setDonations(donationsNum.toString())
                                        .build()

                                    reference.child(username).setValue(userBuilder)
                                    profileViewModel.setUser(userBuilder)
                                    }
                                }

                            override fun onCancelled(error: DatabaseError) {
                                println("Error: $error")
                            }
                        })

                        //val donations = signupPassword.getText().toString().trim { it <= ' ' }

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
//            enable the buttons again
            binding.btnPublish.isEnabled = true
            binding.btnCancel.isEnabled = true
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
//        println(data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val photoUri: Uri? = data?.data
//            print(photoUri)
            imageUri = photoUri
            binding.ivItemImage.setImageURI(photoUri)
        }
    }
}
