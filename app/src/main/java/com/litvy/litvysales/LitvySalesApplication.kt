package com.litvy.litvysales

import android.app.Application
import com.litvy.litvysales.di.AppContainer

class LitvySalesApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()

        container = AppContainer(this)
    }
}