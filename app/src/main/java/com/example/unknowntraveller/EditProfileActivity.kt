package com.example.unknowntraveller

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import de.hdodenhof.circleimageview.CircleImageView

class EditProfileActivity : AppCompatActivity() {
    private lateinit var profilePicture: CircleImageView
    private lateinit var uploadImgButton: Button

    companion object {
        const val PICK_IMAGE_REQUEST = 1
        const val CAMERA_PERMISSION_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        profilePicture = findViewById(R.id.profile_picture)
        uploadImgButton = findViewById(R.id.upload_img)

        uploadImgButton.setOnClickListener {
            showImagePicker()
        }
    }
    private fun showImagePicker() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")

        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Choose your profile picture")

        builder.setItems(options) { dialog, item ->
            when (options[item]) {
                "Take Photo" -> {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        openCamera()
                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.CAMERA),
                            CAMERA_PERMISSION_CODE
                        )
                    }
                }
                "Choose from Gallery" -> {
                    val pickPhoto = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    startActivityForResult(pickPhoto, PICK_IMAGE_REQUEST)
                }
                "Cancel" -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }

    private fun openCamera() {
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePicture, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    if (data != null) {
                        if (data.data != null) {
                            // Image selected from gallery
                            val selectedImageUri = data.data
                            profilePicture.setImageURI(selectedImageUri)
                        } else {
                            // Image captured from camera
                            val imageBitmap = data.extras?.get("data") as Bitmap
                            profilePicture.setImageBitmap(imageBitmap)
                        }
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            }
        }
    }
}