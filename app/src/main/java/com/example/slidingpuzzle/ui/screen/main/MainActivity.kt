package com.example.slidingpuzzle.ui.screen.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.slidingpuzzle.R
import com.example.slidingpuzzle.data.LocalStorage
import com.example.slidingpuzzle.ui.screen.category.CategoryActivity
import com.example.slidingpuzzle.ui.screen.play.PlayActivity
import com.example.slidingpuzzle.utils.PathUtil
import com.example.slidingpuzzle.utils.checkPermission
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var lastSelectedButton: Button? = null
    private var lastSelectedSize = -1
    private var selectedImage = -1
    private var imageFromFolder = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadData()

        val bundle = intent.extras

        if (bundle != null) {
            selectedImage = bundle.getInt("IMAGE")
            imageMain.setImageResource(selectedImage)
        } else {
            selectedImage = R.drawable.cat3
            imageMain.setImageResource(R.drawable.cat3)
        }

    }

    private fun loadData() {
        val size = LocalStorage.instance.selectedSize
        if (size > -1) {
            when (size) {
                3 -> {
                    lastSelectedButton = size3x3
                    lastSelectedSize = 3
                    size3x3.setBackgroundResource(R.drawable.size_back_selected)
                }
                4 -> {
                    lastSelectedSize = 4
                    lastSelectedButton = size4x4
                    size4x4.setBackgroundResource(R.drawable.size_back_selected)
                }
                5 -> {
                    lastSelectedSize = 5
                    lastSelectedButton = size5x5
                    size5x5.setBackgroundResource(R.drawable.size_back_selected)
                }
                6 -> {
                    lastSelectedSize = 6
                    lastSelectedButton = size6x6
                    size6x6.setBackgroundResource(R.drawable.size_back_selected)
                }
            }
        }
        size3x3.setOnClickListener { sizeSelected(size3x3, 3) }
        size4x4.setOnClickListener { sizeSelected(size4x4, 4) }
        size5x5.setOnClickListener { sizeSelected(size5x5, 5) }
        size6x6.setOnClickListener { sizeSelected(size6x6, 6) }
        buttonBrowseGallery.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }
        buttonChooseFromFolder.setOnClickListener {
            checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, 1)
            }
        }
        buttonRandomImage.setOnClickListener {
            imageMain.setImageResource(R.drawable.cat4)
        }
        playButton.setOnClickListener {
            if (selectedImage == -1 || imageFromFolder == "") {
                Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (lastSelectedSize > -1) {
                val intent = Intent(this, PlayActivity::class.java)
                intent.putExtra("IMAGE", selectedImage)
                intent.putExtra("IMAGE_FOLDER", imageFromFolder)
                intent.putExtra("SELECTED_SIZE", lastSelectedSize)
                LocalStorage.instance.selectedSize = -1
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select game size", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun sizeSelected(selected: Button, size: Int) {

        if (lastSelectedButton == selected) return

        if (lastSelectedButton != null) {
            selected.setBackgroundResource(R.drawable.size_back_selected)
            lastSelectedButton!!.setBackgroundResource(R.drawable.size_back_default)
            lastSelectedButton = selected
            lastSelectedSize = size
            LocalStorage.instance.selectedSize = size
            return
        }

        selected.setBackgroundResource(R.drawable.size_back_selected)
        lastSelectedButton = selected
        lastSelectedSize = size
        LocalStorage.instance.selectedSize = size

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val uri = data?.data ?: return
            val path = PathUtil.getPath(this, uri)
            imageFromFolder = path
            selectedImage = -1
            val bitmap = BitmapFactory.decodeFile(path)
            imageMain.setImageBitmap(bitmap)
        }
    }
}
