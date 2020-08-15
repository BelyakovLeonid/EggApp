package com.example.eggyapp

import android.app.Application
import com.example.eggyapp.data.SetupEggRepositoryImpl

class EggApp: Application() {
    companion object{
        val setupRepo = SetupEggRepositoryImpl()
    }
}