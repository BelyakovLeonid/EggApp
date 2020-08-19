package com.example.eggyapp

import android.app.Application
import com.example.eggyapp.di.DaggerAppComponent

class EggApp: Application() {
    companion object{
        val appComponent = DaggerAppComponent.factory().create()
    }
}