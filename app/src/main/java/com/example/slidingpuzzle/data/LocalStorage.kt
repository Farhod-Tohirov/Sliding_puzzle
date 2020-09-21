package com.example.slidingpuzzle.data

import android.content.Context
import android.content.SharedPreferences
import com.example.task1.Tools.IntPreference

class LocalStorage private constructor(context: Context) {
    companion object {
        lateinit var instance: LocalStorage; private set

        fun init(context: Context) {
            instance = LocalStorage(context)
        }
    }

    private val pref: SharedPreferences =
        context.getSharedPreferences("LocalStorage", Context.MODE_PRIVATE)

    var selectedSize by IntPreference(pref, -1)
}