package leo.apps.eggy.root.presentation

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import leo.apps.eggy.R
import leo.apps.eggy.base.di.ViewModelFactory
import leo.apps.eggy.base.utils.isShowing
import leo.apps.eggy.base.utils.showToast
import leo.apps.eggy.timer.TimerService
import javax.inject.Inject

class MainActivity : AppCompatActivity(R.layout.a_main) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    private var toast: Toast? = null

    private val connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {}

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val timerBinder = service as? TimerService.TimerBinder
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
        val intent = Intent(this, TimerService::class.java)
        startService(intent)
        bindService(intent, connection, BIND_AUTO_CREATE)
    }

    private fun setupBackPressedListener() {
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.onBackPressed()
                if (toast.isShowing) {
                    viewModel.onBackPressedConfirmed()
                    finish()
                } else {
                    toast = showToast(getString(R.string.exit_hint))
                }
            }
        })
    }
}