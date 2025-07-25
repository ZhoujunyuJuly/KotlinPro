package com.example.server.realPro.init

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CarHomeApp:Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance : CarHomeApp
            private set
    }
}