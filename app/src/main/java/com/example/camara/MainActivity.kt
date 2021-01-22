package com.example.camara

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream

private const val REQUEST_CODE = 1

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    lateinit var imageView : ImageView
    fun capTure(view: View) {

        var takePicIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (takePicIntent.resolveActivity(this.packageManager)!= null) {
            startActivityForResult(takePicIntent, REQUEST_CODE)
        } else {
            Toast.makeText(this, "Camera can't be opened", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            var takenImage = intent?.extras?.get("data") as Bitmap

            takenImage?.let { takenImage ->
                val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                val imgFile = File(root, "nn.jpg")
                takenImage.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(imgFile))

                val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).apply {
                    data = Uri.fromFile(imgFile)
                }
                this@MainActivity.sendBroadcast(intent)
            }

            imageView.setImageBitmap(takenImage)
        } else {
            super.onActivityResult(requestCode, resultCode, intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
    }
}