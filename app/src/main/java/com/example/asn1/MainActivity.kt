package com.example.asn1

import android.app.Activity
import android.content.ContentValues
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.Audio.Radio
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.os.Environment
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date


class MainActivity : AppCompatActivity() {


    private var uri: Uri? = null
    private var currentImagePath: String = ""
    private lateinit var sharedPref: SharedPreferences
    private var uriToLoad:Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var uriToLoad: Uri

        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE)

        loadProfile()

        val photoBtn = findViewById<Button>(R.id.photo)
        val saveBtn = findViewById<Button>(R.id.save)
        val cancelBtn = findViewById<Button>(R.id.cancel)

        saveBtn.setOnClickListener{
            saveProfile()
        }

        photoBtn.setOnClickListener(){
            val dialogView = LayoutInflater.from(this).inflate(R.layout.choose_photo_dialog, null)
            val dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .create()

            val btnTakePhoto = dialogView.findViewById<Button>(R.id.btnTakePhoto)
            val btnSelectFromGallery = dialogView.findViewById<Button>(R.id.btnSelectFromGallery)

            btnTakePhoto.setOnClickListener {
                useCamera()
                dialog.dismiss()
            }

            btnSelectFromGallery.setOnClickListener {
                selectFromGallery()
                dialog.dismiss()
            }

            dialog.show()
        }
        cancelBtn.setOnClickListener(){
            onCancelButtonClicked()
        }

    }


    private fun selectFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*" // Allow the user to pick any image type
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data

            if (selectedImageUri != null) {
                uriToLoad = selectedImageUri
//                if (uriToLoad != null) {
//                    val editor = sharedPref.edit()
//                    editor.putString("uriStore", uriToLoad.toString())
//                }

                loadImage(selectedImageUri)
            } else {
                Toast.makeText(this, "Sorry, image selected is invalid", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loadImage(imageUri: Uri) {
        val profileImageView = findViewById<ImageView>(R.id.profileImageView)

        profileImageView.setImageURI(imageUri)
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1001
    }





    private fun loadProfile(){
        val nameLoad = sharedPref.getString("nameStore", "").toString()
        val emailLoad = sharedPref.getString("emailStore", "").toString()
        val phoneLoad = sharedPref.getString("phoneStore", "").toString()
        val classNumLoad = sharedPref.getString("classNumStore", "").toString()
        val majorLoad = sharedPref.getString("majorStore", "").toString()

        val selectedButtonId = sharedPref.getInt("genderIdStore", -1)

        if (selectedButtonId != -1) {
            Log.d("gender", selectedButtonId.toString())
            val radioButton = findViewById<RadioButton>(selectedButtonId)

            radioButton.isChecked = true
        }

        val uriLoaded = sharedPref.getString("uriStore", null)

        if (uriLoaded.toString() != null){
            val photo = findViewById<ImageView>(R.id.profileImageView)
            photo.setImageURI(Uri.parse(uriLoaded.toString()))
        }

        findViewById<EditText>(R.id.name_input).setText(nameLoad)
        findViewById<EditText>(R.id.email_input).setText(emailLoad)
        findViewById<EditText>(R.id.phone_input).setText(phoneLoad)
        findViewById<EditText>(R.id.class_input).setText(classNumLoad)
        findViewById<EditText>(R.id.major_input).setText(majorLoad)

        Log.d("load", selectedButtonId.toString())

    }


    private fun saveProfile(){
        val editor = sharedPref.edit()
        val name = findViewById<EditText>(R.id.name_input).getText().toString()
        val email = findViewById<EditText>(R.id.email_input).getText().toString()
        val phone = findViewById<EditText>(R.id.phone_input).getText().toString()
        val radioGroup =  findViewById<RadioGroup>(R.id.radio)

        val selectedRadioId:Int = radioGroup!!.checkedRadioButtonId
        val genderBtn = findViewById<RadioButton>(selectedRadioId)
        val genderBtnId = resources.getResourceEntryName(genderBtn.id).toString()
        val classNum = findViewById<EditText>(R.id.class_input).getText().toString()
        val major = findViewById<EditText>(R.id.major_input).getText().toString()
        val picFile = findViewById<ImageView>(R.id.profileImageView)

        if (uriToLoad != null){
            editor.putString("uriStore", uriToLoad.toString())
        }

        editor.putInt("genderIdStore", selectedRadioId)
        editor.putString("nameStore", name)
        editor.putString("emailStore", email)
        editor.putString("phoneStore", phone)
        editor.putString("classNumStore", classNum)
        editor.putString("majorStore", major)
        editor.apply()
        Toast.makeText(this, "Profile data saved.", Toast.LENGTH_SHORT).show()
        finish()

    }

    private fun useCamera(){

        if (hasCameraPermission() == PERMISSION_GRANTED && hasExternalStoragePermission() == PERMISSION_GRANTED){
            invokeCamera()
        }
        else{
            requestMultiplePermissionLauncher.launch(arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ))

        }
    }

    private val requestMultiplePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()){
        resultsMap ->
        var permissionGranted = false
        resultsMap.forEach{
            if (it.value == true){
                permissionGranted = it.value
            }
            else{
                permissionGranted = false
                return@forEach
            }
        }
        if (permissionGranted){
            invokeCamera()
        }
        else
            Toast.makeText(this, "Unable to load camera without permission", Toast.LENGTH_LONG).show()
    }

    private fun invokeCamera() {
        val file = createImageFile()
        try {
            uri = FileProvider.getUriForFile(this, "com.example.asn1.fileprovider", file)
            getCameraImage.launch(uri)

        }
        catch (e: Exception){
            Log.e(TAG, "Error ${e.message}")
        }
    }

    private val getCameraImage = registerForActivityResult(ActivityResultContracts.TakePicture()){
        success ->
        if (success){
            Log.i(TAG, "Image Location: $uri")
            val profileImageView = findViewById<ImageView>(R.id.profileImageView)
            profileImageView.setImageURI(uri)

            uriToLoad = uri
        }
        else{
            Log.e(TAG, "Image not saved: ${uri}")
        }
    }

    private fun createImageFile():File{
        try {
            val timestamp = SimpleDateFormat("yyyMMdd_HHmmss").format(Date())
            val imageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File.createTempFile(
                "Speciment_${timestamp}",
                ".jpg",
                imageDirectory
            ).apply {
                currentImagePath = absolutePath
            }

        }catch (e:Exception){
            Log.e(TAG, "Error creating image file")
            Toast.makeText(this, "Error creating image file", Toast.LENGTH_LONG).show()
            return File("")
        }
    }

    fun hasCameraPermission() = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
    fun hasExternalStoragePermission() = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    fun onCancelButtonClicked(){
        finish()
    }
}