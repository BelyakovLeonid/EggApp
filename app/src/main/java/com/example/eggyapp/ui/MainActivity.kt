package com.example.eggyapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.eggyapp.R
import com.example.eggyapp.utils.isShowing
import com.example.eggyapp.utils.showToast

class MainActivity : AppCompatActivity() {

    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_main)
        setupBackPressedListener()
    }

    private fun setupBackPressedListener() {
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (toast.isShowing) {
                    finish()
                } else {
                    toast = showToast(getString(R.string.exit_hint))
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        setFullScreenMode()
    }

    private fun setFullScreenMode() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}