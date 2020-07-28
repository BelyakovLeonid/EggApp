package com.example.eggyapp.ui.cook

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.eggyapp.R
import com.example.eggyapp.timer.EggType
import com.example.eggyapp.timer.TimerService
import com.example.eggyapp.timer.TimerService.TimerBinder
import kotlinx.android.synthetic.main.f_egg_cook.*
import java.util.concurrent.TimeUnit

class CookFragment : Fragment(R.layout.f_egg_cook) {

    var timerBinder: TimerBinder? = null

    private val connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("MyTag", "onServiceDisconnected")
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            timerBinder = service as? TimerBinder
            timerBinder?.getProgressLiveData()?.observe(viewLifecycleOwner, Observer {
                view_timer.setCurrentProgress(it.currentProgress, it.timerString)
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleView()
        requireContext().bindService(
            Intent(requireContext(), TimerService::class.java),
            connection,
            Context.BIND_AUTO_CREATE
        )
    }

    private fun handleView() {
        button_control.onPauseListener = {
            timerBinder?.stopTimer()
        }
        button_control.onStartListener = {
            timerBinder?.startTimer(
                TimeUnit.MINUTES.toMillis(2),
                EggType("Soft egg", R.drawable.egg_soft)
            )
        }
        button_back.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireContext().unbindService(connection)
        Log.d("MyTag", "onDestroyView")
    }
}