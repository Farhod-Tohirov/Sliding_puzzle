package com.example.slidingpuzzle.ui.screen.play

import android.graphics.Bitmap
import com.example.slidingpuzzle.model.Coordination

interface GameContract {
    interface Model {
        var space: Coordination
        var step: Int
        var time: String

    }

    interface View {
        fun loadViews()
        fun setScore(score: Int)
        fun startTime()
        fun loadData(data: ArrayList<Pair<Bitmap, String>>)
        fun setElementImage(coordination: Coordination, text: String, Bitmap: Bitmap)
        fun getElementImage(coordination: Coordination): String
        fun clearImage(coordination: Coordination)
        fun showWin()
        fun toHome()
        fun makeAlertDialog(text: String)
        fun showNumbers()
        fun splitImage(imageView: Int, count: Int): ArrayList<Bitmap>
        fun splitImageFromPath(imagePath: String, count: Int): ArrayList<Bitmap>
    }

    interface Presenter {
        fun click(coordination: Coordination)
        fun restart(pos: Int, size: Int)
        fun restart(path: String, size: Int)
        fun clickRestartButton(imageId: Int)
        fun clickRestartButton(imagePath: String)
        fun clickHomeButton()
        fun clickHelp()
        fun clickShowNumbers()
    }
}