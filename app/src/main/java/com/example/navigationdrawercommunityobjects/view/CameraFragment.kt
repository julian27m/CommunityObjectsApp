package com.example.navigationdrawercommunityobjects.view

import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.navigationdrawercommunityobjects.R
import com.example.navigationdrawercommunityobjects.viewmodel.CameraViewModel
import com.example.navigationdrawercommunityobjects.databinding.FragmentCameraBinding

class CameraFragment : Fragment() {
    private val TAG = "CameraFragment"
    private val binding: FragmentCameraBinding by lazy {
        FragmentCameraBinding.inflate(layoutInflater)
    }
    private lateinit var viewModel: CameraViewModel
    private var photoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = binding.root

        viewModel = ViewModelProvider(this).get(CameraViewModel::class.java)

        // Configurar el botón de la cámara
        binding.buttonShutterCapture.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoUri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues()) // crea una Uri vacía para guardar la foto tomada
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            startActivityForResult(takePictureIntent, 1)
        }

//        set camera feed to image view
        binding.previewView.visibility = View.VISIBLE

        // Configurar el botón de aceptar
        binding.buttonAccept.setOnClickListener {
            binding.imageViewPreview.visibility = View.GONE
            binding.buttonAccept.visibility = View.GONE
            binding.buttonRetry.visibility = View.GONE
            binding.buttonShutterCapture.visibility = View.GONE
            binding.previewView.visibility = View.GONE


            parentFragmentManager.setFragmentResult(
                1.toString(),
                Bundle().apply { putString("photoUri", photoUri.toString()) }
            )
            parentFragmentManager.popBackStack()
            parentFragmentManager.beginTransaction().replace(R.id.fragment_container, DonateFragment()).commit()
        }

        // Configurar el botón de reintentar
        binding.buttonRetry.setOnClickListener {
            binding.imageViewPreview.visibility = View.GONE
            binding.buttonAccept.visibility = View.GONE
            binding.buttonRetry.visibility = View.GONE
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            binding.imageViewPreview.visibility = View.VISIBLE
            binding.buttonAccept.visibility = View.VISIBLE
            binding.buttonRetry.visibility = View.VISIBLE
            binding.imageViewPreview.setImageURI(photoUri)
        }
    }
}
