package com.example.community_objects.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.community_objects.R
import com.example.community_objects.databinding.DialogCareersBinding
import com.example.community_objects.databinding.DialogNetworkPostBinding
import com.example.community_objects.databinding.DialogNetworkUpdateBinding
import com.example.community_objects.databinding.DialogRecentUpdatesBinding
import com.example.community_objects.databinding.FragmentEditProfileBinding
import com.example.community_objects.model.NetworkConnection
import com.example.community_objects.model.UserBuilderClass
import com.example.community_objects.viewmodel.ProfileViewModel
import com.google.firebase.database.DataSnapshot
import com.example.community_objects.view.showCareersDialog
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding

    //private val userRef = FirebaseDatabase.getInstance().getReference("users")
    private lateinit var viewModel: ProfileViewModel

    private var pass: Boolean = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        loadFromLocalStorage()
        val titleUsername = binding.titleUsername

        binding.cancelButton.setOnClickListener {
            navigateToProfileFragment()
        }

        viewModel = ProfileViewModel.getInstance()

        var user = viewModel.getUser().value
        val username = user?.username.toString()
        val recentUpdates = user?.updates.toString()
        titleUsername.text = username



        var fallbackBoolean = false

        val networkConnection = NetworkConnection(requireContext())
        networkConnection.observe(viewLifecycleOwner, Observer { isConnected ->
            if (isConnected) {
                if (recentUpdates.toInt() <= 4) {
                    binding.acceptButton.setOnClickListener {
                        updateUserProfile(
                            username,
                            binding.newName.text.toString(),
                            binding.newGender.text.toString(),
                            binding.newAge.text.toString(),
                            binding.newCareer.text.toString()
                        )
                        clearLocalStorage()
                        navigateToProfileFragment()
                    }
                    if (fallbackBoolean) {
                        fallbackBoolean = false
                    } else if (!isConnected) {
                        showNetworkDisconnectedDialog()
                        fallbackBoolean = true
                        saveProfileToLocalStorage(
                            binding.newName.text.toString(),
                            binding.newGender.text.toString(),
                            binding.newAge.text.toString(),
                            binding.newCareer.text.toString()
                        )
                        //return to profile fragment
                        navigateToProfileFragment()
                    }
                }else if (recentUpdates.toInt() > 4){
                showRecentUpdatesDialog()
            }
        }})




        return binding.root
    }

    private fun navigateToProfileFragment() {
        val fragment = ProfileFragment()
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragment_container, fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }

    private fun updateUserProfile(
        username: String,
        newName: String?,
        newGender: String?,
        newAge: String?,
        newCareer: String?
    ) {

        val reference = FirebaseDatabase.getInstance().getReference("users")
        val checkUserDatabase = reference.orderByChild("username").equalTo(username)
        checkUserDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var user = viewModel.getUser().value
                    if (user != null) {
                        // Update the UserBuilderClass instance

                        val updatesNum = ((user.updates?.toInt())?.plus(1)).toString()

                        val userBuilder = UserBuilderClass.Builder()
                            .setName(user.name)
                            .setGender(user.gender)
                            .setAge(user.age)
                            .setEmail(user.email)
                            .setUsername(user.username)
                            .setPassword(user.password)
                            .setDonations(user.donations)
                            .setUpdates(updatesNum)
                            .setCareer(user.career)

                        if (newName != null && newName.trim()
                                .isNotEmpty() && newName.length <= 30
                        ) {
                            user.name = newName.trim()
                            userBuilder.setName(newName.trim())  // update UserBuilderClass instance
                            pass = true
                        } else if (newName != null && newName.trim()
                                .isNotEmpty() && newName.length > 30
                        ) {
                            binding.newName.error = "Name cannot be more than 30 characters"
                            pass = false
                        }
                        if (newGender != null && newGender.trim()
                                .isNotEmpty() && newGender.length <= 30
                        ) {
                            user.gender = newGender.trim().lowercase()
                            userBuilder.setGender(
                                newGender.trim().lowercase()
                            )  // update UserBuilderClass instance
                            pass = true
                        } else if (newGender != null && newGender.trim()
                                .isNotEmpty() && newGender.length > 30
                        ) {
                            binding.newGender.error = "Gender cannot be more than 30 characters"
                            pass = false
                        }
                        if (newAge != null && newAge.trim().isNotEmpty() && isNumeric(newAge)
                            && newAge.length <= 2 && newAge.toInt() >= 16
                        ) {
                            user.age = newAge.trim()
                            userBuilder.setAge(newAge.trim())  // update UserBuilderClass instance
                            pass = true
                        } else if (newAge != null && newAge.trim().isNotEmpty() && isNumeric(newAge)
                            && newAge.length <= 2 && newAge.toInt() < 16
                        ) {
                            binding.newAge.error = "Age must be greater than 16"
                            pass = false
                        } else if (newAge != null && newAge.trim().isNotEmpty() && isNumeric(newAge)
                            && newAge.length > 2
                        ) {
                            binding.newAge.error = "Age cannot be more than 2 digits"
                            pass = false
                        } else if (newAge != null && newAge.trim()
                                .isNotEmpty() && !isNumeric(newAge)
                        ) {
                            binding.newAge.error = "Age must be a number"
                            pass = false
                        }

                        if (newCareer != null && newCareer.trim().isNotEmpty() && isInAndes(
                                newCareer
                            )
                        ) {
                            user.career = newCareer.trim()
                                .replace("á", "a")
                                .replace("é", "e")
                                .replace("í", "i")
                                .replace("ó", "o")
                                .replace("ú", "u")
                                .uppercase()
                            userBuilder.setCareer(user.career)  // update UserBuilderClass instance
                            pass = true
                        } else if (newCareer != null && newCareer.trim().isNotEmpty() && !isInAndes(
                                newCareer
                            )
                        ) {
                            binding.newCareer.error = "Check your career name"
                            pass = false
                        }

                        if (pass) {

                            // Update user profile in the database
                            reference.child(username).setValue(user)
                                .addOnSuccessListener {
                                    // Update the ViewModel's user instance with the newly built UserBuilderClass instance
                                    viewModel.setUser(userBuilder.build())

                                    // Show success message
                                    Toast.makeText(
                                        requireContext(),
                                        "Profile updated successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    // Go back to the profile fragment
                                    navigateToProfileFragment()
                                }
                                .addOnFailureListener { e ->
                                    // Show error message
                                    Toast.makeText(
                                        requireContext(),
                                        "Failed to update profile: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    } else {
                        // User not found
                        Toast.makeText(
                            requireContext(),
                            "User not found",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Database error
                Toast.makeText(
                    requireContext(),
                    "Database error: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun loadFromLocalStorage() {
        val sharedPreferences = requireContext().getSharedPreferences("profile", Context.MODE_PRIVATE)
        binding.newName.setText(sharedPreferences.getString("newName", ""))
        binding.newGender.setText(sharedPreferences.getString("newGender", ""))
        binding.newAge.setText(sharedPreferences.getString("newAge", ""))
        binding.newCareer.setText(sharedPreferences.getString("newCareer", ""))
    }

    private fun saveProfileToLocalStorage(newName: String?, newGender: String?, newAge: String?, newCareer: String?) {
        val sharedPreferences = requireContext().getSharedPreferences("profile", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("newName", newName)
        editor.putString("newGender", newGender)
        editor.putString("newAge", newAge)
        editor.putString("newCareer", newCareer)
        editor.apply()
    }

    private fun showNetworkDisconnectedDialog() {
        val binding = DialogNetworkUpdateBinding.inflate(layoutInflater)
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        val alertDialog = builder.create()
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alertDialog.show()
    }

    private fun showRecentUpdatesDialog() {
        val binding = DialogRecentUpdatesBinding.inflate(layoutInflater)
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        val alertDialog = builder.create()
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alertDialog.show()
    }

    private fun clearLocalStorage() {
        val sharedPreferences = requireContext().getSharedPreferences("profile", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("newName")
        editor.remove("newGender")
        editor.remove("newAge")
        editor.remove("newCareer")
        editor.apply()
    }

    private fun isNumeric(str: String): Boolean {
        return str.matches("-?\\d+(\\.\\d+)?".toRegex())
    }

    fun isInAndes(toCheck: String): Boolean {
        val names = listOf(
            "ESTUDIOS DIRIGIDOS",
            "ADMINISTRACION",
            "ECONOMIA",
            "GOBIERNO Y ASUNTOS PUBLICOS",
            "BIOLOGIA",
            "FISICA",
            "GEOCIENCIAS",
            "MATEMATICAS",
            "MICROBIOLOGIA",
            "QUIMICA",
            "MEDICINA",
            "ARQUITECTURA",
            "DISEÑO",
            "ARTE",
            "HISTORIA DEL ARTE",
            "LITERATURA",
            "MUSICA",
            "NARRATIVAS DIGITALES",
            "INGENIERIA AMBIENTAL",
            "INGENIERIA DE ALIMENTOS",
            "INGENIERIA BIOMEDICA",
            "INGENIERIA CIVIL",
            "INGENIERIA ELECTRICA",
            "INGENIERIA ELECTRONICA",
            "INGENIERIA INDUSTRIAL",
            "INGENIERIA MECANICA",
            "INGENIERIA QUIMICA",
            "INGENIERIA DE SISTEMAS Y COMPUTACION",
            "DERECHO",
            "ANTROPOLOGIA",
            "CIENCIA POLITICA",
            "ESTUDIOS GLOBALES",
            "FILOSOFIA",
            "HISTORIA",
            "LENGUAS Y CULTURA",
            "PSICOLOGIA",
            "LICENCIATURA EN ARTES",
            "LICENCIATURA EN BIOLOGIA",
            "LICENCIATURA EN EDUCACION INFANTIL",
            "LICENCIATURA EN ESPAÑOL Y FILOLOGIA",
            "LICENCIATURA EN FILOSOFIA",
            "LICENCIATURA EN FISICA",
            "LICENCIATURA EN HISTORIA",
            "LICENCIATURA EN MATEMATICAS",
            "LICENCIATURA EN QUIMICA"
        )
        return names.contains(toCheck)
    }



}