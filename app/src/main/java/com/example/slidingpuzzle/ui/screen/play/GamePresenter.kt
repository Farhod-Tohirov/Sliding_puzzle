package com.example.slidingpuzzle.ui.screen.play

import android.graphics.Bitmap
import com.example.slidingpuzzle.model.Coordination
import kotlin.math.abs

class GamePresenter(private val model: GameContract.Model, private val view: GameContract.View) :
    GameContract.Presenter {
    private var list = ArrayList<Pair<Bitmap, String>>()
    private var gameSize = 0

    override fun click(coordination: Coordination) {
        val space: Coordination = model.space
        val dy: Int = abs(space.y - coordination.y)
        val dx: Int = abs(space.x - coordination.x)
        if (dy + dx == 1) {
            model.step = model.step + 1
            view.setScore(model.step)
            val s: String = view.getElementImage(coordination)
            val imageId = getImage(s)
            view.setElementImage(space, s, imageId)
            view.clearImage(coordination)
            model.space = coordination
            if (isWin()) {
                view.showWin()
            }
        }
    }

    override fun restart(pos: Int, size: Int) {
        model.step = 0
        gameSize = size
        view.startTime()
        view.setScore(0)
        val bitmapList = view.splitImage(pos, gameSize*gameSize)
        makeList(bitmapList)
        while (true) {
            if (winAble(list)) break else {
                makeList(bitmapList)
            }
        }
        view.loadData(list)
        model.space = Coordination(
            gameSize - 1,
            gameSize - 1
        )
        view.clearImage(model.space)
    }

    override fun restart(path: String, size: Int) {
        model.step = 0
        gameSize = size
        view.startTime()
        view.setScore(0)
        val bitmapList = view.splitImageFromPath(path, gameSize*gameSize)
        makeList(bitmapList)
        while (true) {
            if (winAble(list)) break else {
                makeList(bitmapList)
            }
        }
        view.loadData(list)
        model.space = Coordination(
            gameSize - 1,
            gameSize - 1
        )
        view.clearImage(model.space)
    }

    override fun clickHomeButton() {
        view.toHome()
    }

    private fun makeList(bitmapList: ArrayList<Bitmap>) {
        list.clear()
        for (i in 0 until bitmapList.size - 1) {
            list.add(Pair(bitmapList[i], "${i + 1}"))
        }
        list.shuffle()
    }

    override fun clickHelp() {
        view.makeAlertDialog("You have to collect all images to right position. If you want you can see numbers to make easy :)\n Creator: Farhod Tohirov\n Telegram: @FarhodTohirov")
    }

    override fun clickShowNumbers() {
        view.showNumbers()
    }

    private fun getImage(s: String): Bitmap {
        for (i in list.indices) {
            if (s == list[i].second) return list[i].first
        }
        return list[list.size].first
    }

    private fun winAble(list: ArrayList<Pair<Bitmap, String>>): Boolean {
        var inverse = 0
        val n = list.size
        for (i in 0 until n - 1) {
            for (j in i + 1 until n) {
                if (list[i].second.toInt() > list[j].second.toInt()) {
                    inverse++
                }
            }
        }
        return inverse % 2 == 0
    }

    private fun isWin(): Boolean {
        if (model.space.x != gameSize - 1 && model.space.y != gameSize - 1) return false
        for (i in 0..gameSize * gameSize - 2) {
            val t = view.getElementImage(
                Coordination(
                    i / gameSize,
                    i % gameSize
                )
            )
            if (t != (i + 1).toString()) {
                return false
            }
        }
        return true
    }

    override fun clickRestartButton(imageId: Int) {
        view.startTime()
        restart(imageId, gameSize)
    }

    override fun clickRestartButton(imagePath: String) {
        view.startTime()
        restart(imagePath, gameSize)
    }
}