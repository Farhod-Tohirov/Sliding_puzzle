package com.example.slidingpuzzle.app

import android.app.Application
import com.example.slidingpuzzle.data.LocalStorage

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        LocalStorage.init(this)
    }
}