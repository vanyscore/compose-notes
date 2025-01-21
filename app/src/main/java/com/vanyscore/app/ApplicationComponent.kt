package com.vanyscore.app

import android.app.Application

class ApplicationComponent : Application() {
    override fun onCreate() {
        super.onCreate()

        Services.bindApplicationContext(this)
        Services.build()
    }
}