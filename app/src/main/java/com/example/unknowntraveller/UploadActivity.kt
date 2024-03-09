package com.example.unknowntraveller

import android.app.AlertDialog
import android.app.Instrumentation
import android.content.Intent
import android.icu.text.DateFormat
import android.icu.util.Calendar
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.unknowntraveller.databinding.ActivityMainBinding
import com.example.unknowntraveller.databinding.ActivityUploadBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    var imageURI : String? = null
    var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()){result ->
            if(result.resultCode == RESULT_OK){
                val data = result.data
                uri =  data!!.data
                binding.uploadImage.setImageURI(uri)
            }else{
                Toast.makeText(this@UploadActivity,"No Image Selected" , Toast.LENGTH_SHORT).show()
            }
        }
        binding.uploadImage.setOnClickListener{
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }
        binding.saveButton.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {
        val storageReferance = FirebaseStorage.getInstance().reference.child("Task Images")
            .child(uri!!.lastPathSegment!!)
        val builder = AlertDialog.Builder(this@UploadActivity)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
        dialog.show()

        storageReferance.putFile(uri!!).addOnSuccessListener { taskSnapshot ->
            val uriTask = taskSnapshot.storage.downloadUrl
            while(!uriTask.isComplete);
            val uriImage = uriTask.result
            imageURI = uriImage.toString()
            uploadData()
            dialog.dismiss()

        }.addOnFailureListener{
            dialog.dismiss()
        }
    }
    private fun uploadData(){
        val title = binding.uploadTitle.text.toString()
        val desc = binding.uploadDesc.text.toString()
        val priority = binding.uploadPriority.text.toString()

        val dataClass = DataClass(title, desc,priority,imageURI)
        val currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)

        FirebaseDatabase.getInstance().getReference("Destination").child(currentDate)
            .setValue(dataClass).addOnCompleteListener{task ->
                if(task.isSuccessful){
                    Toast.makeText(this@UploadActivity,"Saved",Toast.LENGTH_SHORT).show()
                    finish()
                }

            }.addOnFailureListener{e->
                Toast.makeText(this@UploadActivity,e.message.toString(),Toast.LENGTH_SHORT).show()
            }
    }
}

