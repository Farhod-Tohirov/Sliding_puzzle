package com.example.slidingpuzzle.ui.screen.play

import android.app.AlertDialog
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.slidingpuzzle.R
import com.example.slidingpuzzle.model.Coordination
import kotlinx.android.synthetic.main.activity_play.*
import kotlinx.android.synthetic.main.dialog.*
import kotlin.math.sqrt

class PlayActivity : AppCompatActivity(), GameContract.View {

    private val images = Array(6) { arrayOfNulls<Button>(6) }
    private lateinit var timer: Chronometer
    private lateinit var step: TextView
    private var imageId = -1
    private var imagePath = ""
    private var showedNumbers = false
    private var size = 0
    private val presenter =
        GamePresenter(
            GameRepository(),
            this
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        val bundle = intent.extras
        if (bundle != null) {
            imageId = bundle.getInt("IMAGE")
            imagePath = bundle.getString("IMAGE_FOLDER", "")
            size = bundle.getInt("SELECTED_SIZE")
        }

        loadViews()
        if (imageId != -1)
            presenter.restart(imageId, size) else presenter.restart(imagePath, size)
    }

    override fun loadViews() {
        step = findViewById(R.id.steps)
        timer = findViewById(R.id.timer)
        buttonHome.setOnClickListener { makeSuggestDialog("Exit game?") }
        buttonRestartGame.setOnClickListener { makeSuggestDialog("Restart?") }
        buttonShowNumbers.setOnClickListener { presenter.clickShowNumbers() }
        buttonSupport.setOnClickListener { presenter.clickHelp() }

        for (i in 0 until group.childCount) {
            images[i / 6][i % 6] = group.getChildAt(i) as Button
            group.getChildAt(i).isClickable = true
            if (i / 6 >= size || i % 6 >= size) {
                images[i / 6][i % 6]?.visibility = View.GONE
                Log.d("T12T", "here GONE")
            }
            images[i / 6][i % 6]!!.tag =
                Coordination(i / 6, i % 6)
            images[i / 6][i % 6]!!.setOnClickListener {
                presenter.click(it.tag as Coordination)
            }
        }
    }

    override fun setScore(score: Int) {
        step.text = score.toString()
    }

    override fun startTime() {
        timer.stop()
        timer.base = SystemClock.elapsedRealtime()
        timer.start()
        group.isClickable = true
        for (i in 0 until group.childCount) {
            group.getChildAt(i).isClickable = true
        }
    }

    override fun loadData(data: ArrayList<Pair<Bitmap, String>>) {
        for (i in data.indices) {
            val d = data[i]
            images[i / size][i % size]!!.background = BitmapDrawable(d.first)
            images[i / size][i % size]!!.text = d.second
        }
    }

    override fun setElementImage(coordination: Coordination, text: String, imageId: Bitmap) {
        images[coordination.x][coordination.y]!!.background = BitmapDrawable(imageId)
        images[coordination.x][coordination.y]!!.text = text
    }

    override fun getElementImage(coordination: Coordination): String {
        return images[coordination.x][coordination.y]!!.text.toString()
    }

    override fun clearImage(coordination: Coordination) {
        images[coordination.x][coordination.y]!!.setBackgroundResource(R.drawable.transparent_back)
        images[coordination.x][coordination.y]!!.text = ""
    }

    override fun showWin() {
        if (imageId != -1)
            group.setBackgroundResource(imageId) else {
            val drawable = BitmapDrawable.createFromPath(imagePath)
            group.background = drawable
        }
        timer.stop()
        group.isClickable = false
        for (i in 0 until group.childCount) {
            group.getChildAt(i).isClickable = false
        }
        AlertDialog.Builder(this)
            .setTitle("You are genius :)")
            .setMessage("You have completed this task in ${timer.text} seconds and ${step.text} steps")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun makeSuggestDialog(text: String) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog, null)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val d = AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .show()
            d.positive_button.setOnClickListener {
                if (text == "Restart?") {
                    if (imageId != -1)
                        presenter.clickRestartButton(imageId) else presenter.clickRestartButton(
                        imagePath
                    )
                    d.dismiss()
                    Toast.makeText(this, "Restarted", Toast.LENGTH_SHORT).show()
                } else {
                    presenter.clickHomeButton()
                    d.dismiss()
                }
            }
            d.negative_button.setOnClickListener { d.dismiss() }
            d.textSuggest.text = text

        } else {
            AlertDialog.Builder(this)
                .setTitle(text)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok") { dialog, which ->
                    when (which) {
                        AlertDialog.BUTTON_POSITIVE -> {
                            if (text == "Restart?") {
                                if (imageId != -1)
                                    presenter.clickRestartButton(imageId) else presenter.clickRestartButton(imagePath)
                                dialog.dismiss()
                                Toast.makeText(this, "Restarted", Toast.LENGTH_SHORT).show()
                            } else {
                                presenter.clickHomeButton()
                                dialog.dismiss()
                            }
                        }
                    }
                }.show()
        }
    }

    override fun toHome() {
        finish()
    }

    override fun makeAlertDialog(text: String) {
        AlertDialog.Builder(this)
            .setMessage(text)
            .setPositiveButton("Ok", null)
            .show()
    }

    override fun showNumbers() {
        showedNumbers = !showedNumbers
        for (i in 0 until group.childCount) {
            val t = group.getChildAt(i) as Button
            if (showedNumbers) {
                t.setTextColor(Color.CYAN)
            } else {
                t.setTextColor(resources.getColor(R.color.transparent))
            }
        }
    }

    override fun splitImage(imageView: Int, count: Int): ArrayList<Bitmap> {
        group.setBackgroundResource(R.drawable.size_back_image_change)
        val rows: Int
        val chunkHeight: Int
        val chunkWidth: Int
        val chunkedImages: ArrayList<Bitmap>?
        chunkedImages = ArrayList(count)
        val bitmap = resizedBitmap(imageView, 312, 312)!!
        val scaledBitmap =
            Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, true)
        val cols: Int = sqrt(count.toDouble()).toInt()
        rows = cols
        chunkHeight = bitmap.height / rows
        chunkWidth = bitmap.width / cols
        var yCoordinate = 0
        for (x in 0 until rows) {
            var xCoord = 0
            for (y in 0 until cols) {
                chunkedImages.add(
                    Bitmap.createBitmap(
                        scaledBitmap,
                        xCoord,
                        yCoordinate,
                        chunkWidth,
                        chunkHeight
                    )
                )
                xCoord += chunkWidth
            }
            yCoordinate += chunkHeight
        }
        return chunkedImages
    }

    override fun splitImageFromPath(imagePath: String, count: Int): ArrayList<Bitmap> {
        group.setBackgroundResource(R.drawable.size_back_image_change)
        val rows: Int
        val chunkHeight: Int
        val chunkWidth: Int
        val chunkedImages: ArrayList<Bitmap>?
        chunkedImages = ArrayList(count)
        val bitmap = resizedBitmap(imagePath, 312, 312)!!
        val scaledBitmap =
            Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, true)
        val cols: Int = sqrt(count.toDouble()).toInt()
        rows = cols
        chunkHeight = bitmap.height / rows
        chunkWidth = bitmap.width / cols
        var yCoordinate = 0
        for (x in 0 until rows) {
            var xCoord = 0
            for (y in 0 until cols) {
                chunkedImages.add(
                    Bitmap.createBitmap(
                        scaledBitmap,
                        xCoord,
                        yCoordinate,
                        chunkWidth,
                        chunkHeight
                    )
                )
                xCoord += chunkWidth
            }
            yCoordinate += chunkHeight
        }
        return chunkedImages
    }

    private fun resizedBitmap(imageView: Int, newHeight: Int, newWidth: Int): Bitmap? {
        sourceImage.setImageResource(imageView)
        val drawable = sourceImage.drawable as BitmapDrawable
        val bm = drawable.bitmap
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(
            bm, 0, 0, width, height,
            matrix, false
        )
    }

    private fun resizedBitmap(imagePath: String, newHeight: Int, newWidth: Int): Bitmap? {
        val bitmap = BitmapFactory.decodeFile(imagePath)
        sourceImage.setImageBitmap(bitmap)
        val drawable = sourceImage.drawable as BitmapDrawable
        val bm = drawable.bitmap
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(
            bm, 0, 0, width, height,
            matrix, false
        )
    }
}

