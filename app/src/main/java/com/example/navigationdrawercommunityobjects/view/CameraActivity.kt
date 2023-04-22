package com.example.navigationdrawercommunityobjects.view

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.camera.core.ImageCaptureException
import com.example.navigationdrawercommunityobjects.view.Constants
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.UserDictionary.Words.LOCALE
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.navigationdrawercommunityobjects.R
import com.example.navigationdrawercommunityobjects.databinding.ActivityCameraBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Objects
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {

    private var photoUri: Uri? = null
    private lateinit var binding: ActivityCameraBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File

    private lateinit var cameraExecutorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Im using this to unable landscape mode
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        outputDirectory = getOutputDirectory()
        cameraExecutorService = Executors.newSingleThreadExecutor()

        if(allPermissionGranted()) {
            //Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show()
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, Constants.REQUIRED_PERMISSIONS,
                Constants.REQUEST_CODE_PERMISSIONS)
        }

        binding.btnTakePhoto.setOnClickListener(){
            takePhoto()
        }
        binding.buttonAccept.setOnClickListener(){
            returnPhoto()
        }
        binding.buttonRetry.setOnClickListener(){
            startCamera()
        }
        binding.btnCancel.setOnClickListener(){
            finish()
        }

    }

    private fun returnPhoto() {
//        println("-----------------------------------------------------------------------------------------------------------------------photoUri: $photoUri")
        val intent = Intent()
        intent.setData(photoUri)
        setResult(RESULT_OK, intent)
        finish()
    }


    private fun getOutputDirectory():File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(Constants.FILE_NAME_FORMAT, Locale.getDefault()).format(System.currentTimeMillis()) + ".jpg"
        )
        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOption, ContextCompat.getMainExecutor(this),
            object: ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    photoUri = savedUri
                    val msg = "Photo Saved"
                    Toast.makeText(this@CameraActivity,"$msg $savedUri", Toast.LENGTH_SHORT).show()

                    val btnAccept = binding.buttonAccept
                    val btnRetry = binding.buttonRetry
                    val imgPreview = binding.imagePreview
                    val btnShutter = binding.btnTakePhoto
                    val livePreview = binding.viewFinder
                    btnAccept.visibility = android.view.View.VISIBLE
                    btnRetry.visibility = android.view.View.VISIBLE
                    imgPreview.setImageURI(savedUri)
                    imgPreview.visibility = android.view.View.VISIBLE
                    btnShutter.visibility = android.view.View.INVISIBLE
                    livePreview.visibility = android.view.View.INVISIBLE


                }
                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraActivity", "Error: ${exception.message}", exception)
                }
            }
        )
    }



    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({

            val btnAccept = binding.buttonAccept
            val btnRetry = binding.buttonRetry
            val imgPreview = binding.imagePreview
            val btnShutter = binding.btnTakePhoto
            val livePreview = binding.viewFinder

            btnAccept.visibility = android.view.View.INVISIBLE
            btnRetry.visibility = android.view.View.INVISIBLE
            imgPreview.visibility = android.view.View.INVISIBLE
            btnShutter.visibility = android.view.View.VISIBLE
            livePreview.visibility = android.view.View.VISIBLE

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also { mPreview->
                mPreview.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try{

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            }catch (e: Exception){
                Log.d("CameraActivity", "Error: ${e.message}")
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }

        }, ContextCompat.getMainExecutor(this))

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.REQUEST_CODE_PERMISSIONS){
            if (allPermissionGranted()){
                Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show()
                startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }



    private fun allPermissionGranted() =
        Constants.REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                baseContext, it
            ) == PackageManager.PERMISSION_GRANTED
        }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutorService.shutdown()
    }

}