package com.example.eggyapp.ui.cook

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.eggyapp.EggApp
import com.example.eggyapp.R
import com.example.eggyapp.data.SetupType.MEDIUM_TYPE
import com.example.eggyapp.data.SetupType.SOFT_TYPE
import com.example.eggyapp.databinding.FEggCookBinding
import com.example.eggyapp.timer.TimerService
import com.example.eggyapp.timer.TimerService.TimerBinder
import com.example.eggyapp.ui.base.BaseFragment
import com.example.eggyapp.ui.views.ButtonState
import com.example.eggyapp.utils.getColor
import com.example.eggyapp.utils.observeLiveData
import com.example.eggyapp.utils.showToast
import com.example.eggyapp.utils.toTimerString
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

private const val FINISH_ANIMATION_DELAY = 200L

class CookFragment : BaseFragment(R.layout.f_egg_cook) {

    private var timerBinder: TimerBinder? = null

    private val viewModel: CookViewModel by viewModels { viewModelFactory }
    private val binding by viewBinding(FEggCookBinding::bind)

    private val connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            timerBinder = service as? TimerBinder
            if (timerBinder?.isRunning == true) {
                binding.buttonControl.setState(ButtonState.STATE_STARTED)
            }
            observeLiveData(timerBinder?.progress) {
                binding.viewTimer.setProgress(it)
            }
            observeLiveData(timerBinder?.timerText) {
                binding.viewTimer.setTimerText(it)
            }
            observeLiveData(timerBinder?.finish) {
                showFinish()
            }
            observeLiveData(timerBinder?.cancel) {
                binding.buttonControl.setState(ButtonState.STATE_IDLED)
                binding.viewTimer.dropProgress()
            }
            observeLiveData(viewModel.calculatedTime) {
                timerBinder?.setTime(it.toLong())
            }
            observeLiveData(viewModel.selectedType) {
                timerBinder?.setType(it)
            }
        }
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            showExitDialog()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as EggApp).appComponent.inject(this)
        requireActivity().onBackPressedDispatcher.addCallback(backPressedCallback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleView()
        observeViewModel()
        requireContext().bindService(
            Intent(requireContext(), TimerService::class.java),
            connection,
            Context.BIND_AUTO_CREATE
        )
    }

    private fun observeViewModel() {
        with(viewModel) {
            observeLiveData(selectedType) {
                binding.textCookTitle.text = when (it) {
                    SOFT_TYPE -> getString(R.string.cook_eggs_soft)
                    MEDIUM_TYPE -> getString(R.string.cook_eggs_medium)
                    else -> getString(R.string.cook_eggs_hard)
                }
            }
            observeLiveData(calculatedTime) {
                binding.textTime.text = it.toTimerString()
                binding.viewTimer.setTimerText(it.toTimerString())
            }
        }
    }

    private fun handleView() {
        binding.buttonControl.onCancelListener = {
            timerBinder?.stopTimer()
        }
        binding.buttonControl.onStartListener = {
            timerBinder?.startTimer()
        }
        binding.buttonBack.setOnClickListener {
            showExitDialog()
        }
    }

    private fun showExitDialog() {
        val dialog = ExitDialog.getInstance()
        dialog.onConfirmListener = {
            timerBinder?.stopTimer()
            findNavController().navigateUp()
        }
        dialog.show(childFragmentManager, ExitDialog.TAG)
    }

    private fun showFinish() {
        binding.buttonControl.setState(ButtonState.STATE_IDLED)
        binding.viewTimer.dropProgress(FINISH_ANIMATION_DELAY)
        context?.showToast(getString(R.string.toast_finish_text))
        makeVibration()
        makeConfetti()
    }

    private fun makeVibration() {
        val vibratorService = context?.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        if (vibratorService?.hasVibrator() == true) {
            val vibrationEffect = VibrationEffect.createWaveform(
                longArrayOf(0, 50, 50, 50, 50, 200, 100, 200, 100, 200),
                intArrayOf(0, 100, 0, 100, 0, 220, 0, 220, 0, 220),
                -1
            )
            vibratorService.vibrate(vibrationEffect)
        }
    }

    private fun makeConfetti() {
        binding.viewConfetti.build()
            .addColors(
                getColor(R.color.confetti_yellow),
                getColor(R.color.confetti_orange),
                getColor(R.color.confetti_purple),
                getColor(R.color.confetti_pink)
            )
            .setDirection(0.0, 359.0)
            .setSpeed(1f, 5f)
            .setFadeOutEnabled(true)
            .setTimeToLive(2000L)
            .addShapes(Shape.Square, Shape.Circle)
            .addSizes(Size(12))
            .setPosition(-50f, binding.viewConfetti.width + 50f, -50f, -50f)
            .streamFor(150, 2000L)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireContext().unbindService(connection)
    }

    override fun onDetach() {
        backPressedCallback.isEnabled = false
        super.onDetach()
    }
}