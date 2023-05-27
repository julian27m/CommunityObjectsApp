package com.example.community_objects.view

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider


import com.example.community_objects.databinding.DialogNetworkPostBinding
import com.example.community_objects.databinding.FragmentDonateBinding
import com.example.community_objects.model.NetworkConnection
import com.example.community_objects.model.Post
import com.example.community_objects.model.UserBuilderClass
import com.example.community_objects.viewmodel.ItemViewModel
import com.example.community_objects.viewmodel.ProfileViewModel
import com.example.community_objects.R
import com.google.firebase.database.*
import com.google.gson.Gson

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


        var fallbackBoolean = false

        val networkConnection = NetworkConnection(requireContext())
        networkConnection.observe(viewLifecycleOwner, Observer { isConnected ->
            if (isConnected) {
                if (fallbackBoolean) {
                    //Log.d("MainActivity", "NetworkConnection: isConnected")
                    //dismissNetworkDialog()
                    fallbackBoolean = false
                }
                //Log.d("MainActivity", "NetworkConnection: isConnected")
                //showNetworkDialog()
            } else {
                //Log.d("MainActivity", "NetworkConnection: isNotConnected")
                showNetworkDisconnectedDialog()
                fallbackBoolean = true
                savePostToLocaLStorage()
//                return to the home fragment
                val homeFragment = HomeFragment()
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, homeFragment)
                transaction.commit()
            }
        })

        loadFromLocalStorage()

        // Configurar el botón de agregar item
        binding.btnPublish.setOnClickListener {
//            disable the buttons and make them invisible
            disableButtons()

//            check the min length of the visible Text
            if (binding.etItemName.text.length < 3 && binding.etItemName.visibility == View.VISIBLE) {
                binding.etItemName.error = "Name must be at least 3 characters long"
                enableButtons()
                return@setOnClickListener
            }
            if (binding.etItemDescription.text.length < 20 && binding.etItemDescription.visibility == View.VISIBLE) {
                binding.etItemDescription.error = "Description must be at least 20 characters long"
                enableButtons()
                return@setOnClickListener
            }
            if (binding.etItemDegree.text.length < 3 && binding.etItemDegree.visibility == View.VISIBLE) {
                binding.etItemDegree.error = "Degree must be at least 3 characters long"
                enableButtons()
                return@setOnClickListener
            }
            if (binding.etItemType.text.length < 3 && binding.etItemType.visibility == View.VISIBLE) {
                binding.etItemType.error = "Type must be at least 3 characters long"
                enableButtons()
                return@setOnClickListener
            }
            if (binding.etItemAuthor.text.isEmpty() && binding.etItemAuthor.visibility == View.VISIBLE) {
                binding.etItemAuthor.error = "Author must not be empty"
                enableButtons()
                return@setOnClickListener
            }
            if (binding.etItemSubject.text.length < 3 && binding.etItemSubject.visibility == View.VISIBLE) {
                binding.etItemSubject.error = "Subject must be at least 3 characters long"
                enableButtons()
                return@setOnClickListener
            }
            if (binding.etItemColors.text.length < 3 && binding.etItemColors.visibility == View.VISIBLE) {
                binding.etItemColors.error = "Colors must be at least 3 characters long"
                enableButtons()
                return@setOnClickListener
            }
            if (binding.etItemSize.text.isEmpty() && binding.etItemSize.visibility == View.VISIBLE) {
                binding.etItemSize.error = "Size can't be empty"
                enableButtons()
                return@setOnClickListener
            }
            if (binding.etItemReference.text.length < 3 && binding.etItemReference.visibility == View.VISIBLE) {
                binding.etItemReference.error = "Reference must be at least 3 characters long"
                enableButtons()
                return@setOnClickListener
            }

//          check that any of the visible text input has errors
            if (binding.etItemName.error != null && binding.etItemName.visibility == View.VISIBLE) {
                enableButtons()
                return@setOnClickListener
            } else if (binding.etItemDescription.error != null && binding.etItemDescription.visibility == View.VISIBLE) {
                enableButtons()
                return@setOnClickListener
            } else if (binding.etItemDegree.error != null && binding.etItemDegree.visibility == View.VISIBLE) {
                enableButtons()
                return@setOnClickListener
            } else if (binding.etItemType.error != null && binding.etItemType.visibility == View.VISIBLE) {
                enableButtons()
                return@setOnClickListener
            } else if (binding.etItemAuthor.error != null && binding.etItemAuthor.visibility == View.VISIBLE) {
                enableButtons()
                return@setOnClickListener
            } else if (binding.etItemSubject.error != null && binding.etItemSubject.visibility == View.VISIBLE) {
                enableButtons()
                return@setOnClickListener
            } else if (binding.etItemColors.error != null && binding.etItemColors.visibility == View.VISIBLE) {
                enableButtons()
                return@setOnClickListener
            } else if (binding.etItemSize.error != null && binding.etItemSize.visibility == View.VISIBLE) {
                enableButtons()
                return@setOnClickListener
            } else if (binding.etItemReference.error != null && binding.etItemReference.visibility == View.VISIBLE) {
                enableButtons()
                return@setOnClickListener
            }

            disableButtons()


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
            item["user"] = username
            item["title"] = binding.etItemName.text.toString()

            if (imageUri != null) {
                disableButtons()
                itemViewModel.addItem(item, imageUri!!) { success ->
//                    println("intenta publicar")
                    if (success) {
                        disableButtons()
                        val checkUserDatabase = reference.orderByChild("username").equalTo(username)
                        checkUserDatabase.addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    val nameFromDB =
                                        snapshot.child(username).child("name").getValue(
                                            String::class.java
                                        )
                                    val passwordFromDB =
                                        snapshot.child(username).child("password").getValue(
                                            String::class.java
                                        )
                                    val emailFromDB =
                                        snapshot.child(username).child("email").getValue(
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
                        Toast.makeText(
                            requireContext(),
                            "Item added successfully",
                            Toast.LENGTH_SHORT

                        )
                            .show()
                    }
                    else {
                        Toast.makeText(
                            requireContext(),
                            "Error adding the item",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
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
                enableButtons()
            }
            enableButtons()
        }
        binding.btnCancel.setOnClickListener {
            //            set the home fragment
            val supportFragmentManager = requireActivity().supportFragmentManager
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commit()
        }

        binding.spCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (binding.spCategory.selectedItem.toString()) {
                    "Clothes" -> {
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
                binding.spCategory.setSelection(0)
            }
        }

        binding.btnAddImage.setOnClickListener {
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

    private fun disableButtons() {
        binding.btnPublish.isEnabled = false
        binding.btnCancel.isEnabled = false
        binding.btnPickImage.isEnabled = false
        binding.btnAddImage.isEnabled = false

        binding.btnPublish.visibility = View.INVISIBLE
        binding.btnCancel.visibility = View.INVISIBLE
    }

    private fun enableButtons() {
        binding.btnPublish.isEnabled = true
        binding.btnCancel.isEnabled = true
        binding.btnPickImage.isEnabled = true
        binding.btnAddImage.isEnabled = true

        binding.btnCancel.visibility = View.VISIBLE
        binding.btnPublish.visibility = View.VISIBLE
    }

    private fun savePostToLocaLStorage() {
        val title = binding.etItemName.text.toString()
        val description = binding.etItemDescription.text.toString()
        val category = binding.spCategory.selectedItem.toString()
        val type = binding.etItemType.text.toString()
        val degree = binding.etItemDegree.text.toString()
        val author = binding.etItemAuthor.text.toString()
        val subject = binding.etItemSubject.text.toString()
        val colors = binding.etItemColors.text.toString()
        val size = binding.etItemSize.text.toString()
        val reference = binding.etItemReference.text.toString()
        val image = imageUri.toString()

        val post = Post(
            title = title,
            description = description,
            category = category,
            type = type,
            degree = degree,
            author = author,
            subject = subject,
            colors = colors,
            size = size,
            reference = reference,
            image = image
        )

        val postJson = Gson().toJson(post)
        val sharedPreferences = requireContext().getSharedPreferences("post", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("post", postJson)
        editor.apply()
    }

    private fun loadFromLocalStorage() {
        val sharedPreferences = requireContext().getSharedPreferences("post", Context.MODE_PRIVATE)
        val postJson = sharedPreferences.getString("post", null)
        if (postJson != null) {
            val post = Gson().fromJson(postJson, Post::class.java)
            binding.etItemName.setText(post.title)
            binding.etItemDescription.setText(post.description)
            binding.spCategory.setSelection(getIndex(binding.spCategory, post.category))
            binding.etItemType.setText(post.type)
            binding.etItemDegree.setText(post.degree)
            binding.etItemAuthor.setText(post.author)
            binding.etItemSubject.setText(post.subject)
            binding.etItemColors.setText(post.colors)
            binding.etItemSize.setText(post.size)
            binding.etItemReference.setText(post.reference)
            binding.ivItemImage.setImageURI(Uri.parse(post.image))
            this.imageUri = Uri.parse(post.image)
        }
//        delete the post from local storage
        val editor = sharedPreferences.edit()
        editor.remove("post")
        editor.apply()
    }

    private fun getIndex(spCategory: Spinner, category: String): Int {
        for (i in 0 until spCategory.count) {
            if (spCategory.getItemAtPosition(i).toString().equals(category, ignoreCase = true)) {
                return i
            }
        }
        return 0
    }

    private fun showNetworkDisconnectedDialog() {
        val binding = DialogNetworkPostBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        val alertDialog = builder.create()
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alertDialog.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK) {
            val photoUri: Uri? = data?.data
            imageUri = photoUri
            binding.ivItemImage.setImageURI(photoUri)
        }
    }
}
