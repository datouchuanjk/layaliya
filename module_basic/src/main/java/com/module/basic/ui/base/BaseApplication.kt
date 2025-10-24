package com.module.basic.ui.base

import android.app.Application

abstract class BaseApplication : Application() {
    companion object {
        lateinit var INSTANCE: Application
    }
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}