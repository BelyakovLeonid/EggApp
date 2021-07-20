package leo.apps.eggy.base.presentation

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import leo.apps.eggy.R
import leo.apps.eggy.base.utils.isShowing
import leo.apps.eggy.base.utils.showToast
import leo.apps.eggy.timer.TimerService

class MainActivity : AppCompatActivity(R.layout.a_main) {

    private var toast: Toast? = null

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
        WindowCompat.setDecorFitsSystemWindows(window, false)
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
}