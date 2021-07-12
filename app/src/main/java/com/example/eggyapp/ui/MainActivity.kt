package com.example.eggyapp.ui

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.eggyapp.R
import com.example.eggyapp.databinding.AMainBinding
import com.example.eggyapp.timer.TimerService
import com.example.eggyapp.utils.isShowing
import com.example.eggyapp.utils.showToast

class MainActivity : AppCompatActivity(R.layout.a_main) {

    private var toast: Toast? = null
    private val binding by viewBinding(AMainBinding::bind)

    private var timerBinder: TimerService.TimerBinder? = null
    private val connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            timerBinder = service as? TimerService.TimerBinder
            if (timerBinder?.isRunning == true) {
                findNavController(R.id.mainContainer).let {
                    it.setGraph(R.navigation.content_graph)
                    it.navigate(R.id.cookFragment)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBackPressedListener()
        startTimerService()
    }

    private fun startTimerService() {
        startService(Intent(this, TimerService::class.java)) //todo проверить
        bindService(
            Intent(this, TimerService::class.java),
            connection,
            BIND_AUTO_CREATE
        )
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

    override fun onResume() {
        super.onResume()
        setFullScreenMode()
    }

    private fun setFullScreenMode() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.mainContainer).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}