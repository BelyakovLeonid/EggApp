package com.example.eggyapp

import android.app.Application
import com.example.eggyapp.di.DaggerAppComponent

class EggApp : Application() {

    val appComponent by lazy {
        DaggerAppComponent.factory().create()
    }
}