package com.example.storage_activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.storage_activity.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.FileOutputStream
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    val pdf: Int = 111
    lateinit var uri: Uri

    var downloadUrl: String = "gs://cloud-iug-6573a.appspot.com/File/pdf-file"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = (View.INVISIBLE)


        val storage = Firebase.storage
        val ref = storage.reference

        binding.add.setOnClickListener { view: View ->
            val intent = Intent()
            intent.type="application/pdf"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), pdf)
        }

        binding.upload.setOnClickListener {

            binding.progressBar.visibility = (View.VISIBLE)

            ref.child("File/" + UUID.randomUUID().toString())
                .putFile(uri)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext,"uploaded successfully", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = (View.INVISIBLE)

                }
                .addOnFailureListener{
                    Toast.makeText(applicationContext,"failed upload", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = (View.INVISIBLE)

                }
        }

        binding.download.setOnClickListener {

            binding.progressBar.visibility = (View.VISIBLE)


            val storageRef = FirebaseStorage.getInstance().getReference("File/pdf-file.pdf")

            val localFile = File.createTempFile("myfile", ".pdf")

            storageRef.getBytes(1024 * 1024).addOnSuccessListener { bytes ->
                FileOutputStream(localFile).apply {
                    write(bytes)
                    close()

                    Toast.makeText(applicationContext,"success ", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = (View.INVISIBLE)
                }
            }.addOnFailureListener {
                    Toast.makeText(applicationContext,"failed ", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = (View.INVISIBLE)


            }



        }

        }

    override fun onActivityResult(requestCode: Int, resultCode
    : Int, data: Intent?) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == pdf) {
                uri = data!!.data!!
                binding.txt.text = uri.toString()
                binding.img.setImageResource(R.drawable.added)
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }


}