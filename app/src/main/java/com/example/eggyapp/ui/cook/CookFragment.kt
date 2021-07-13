package com.example.eggyapp.ui.cook

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.eggyapp.EggApp
import com.example.eggyapp.R
import com.example.eggyapp.base.VibratorManager
import com.example.eggyapp.base.utils.makeDefaultConfetti
import com.example.eggyapp.data.SetupType.MEDIUM_TYPE
import com.example.eggyapp.data.SetupType.SOFT_TYPE
import com.example.eggyapp.databinding.FEggCookBinding
import com.example.eggyapp.timer.TimerService
import com.example.eggyapp.timer.TimerService.TimerBinder
import com.example.eggyapp.ui.base.BaseFragment
import com.example.eggyapp.ui.views.ButtonState
import com.example.eggyapp.base.utils.observeFlow
import com.example.eggyapp.base.utils.showToast
import com.example.eggyapp.base.utils.toTimerString

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

            timerBinder?.let {
                observeFlow(it.progress) {
                    binding.viewTimer.setProgress(it)
                }
                observeFlow(it.timerText) {
                    binding.viewTimer.setTimerText(it)
                }
                observeFlow(it.finish) {
                    showFinish()
                }
                observeFlow(it.cancel) {
                    binding.buttonControl.setState(ButtonState.STATE_IDLE)
                    binding.viewTimer.dropProgress()
                }
                observeViewModel()
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
        requireContext().bindService(
            Intent(requireContext(), TimerService::class.java),
            connection,
            Context.BIND_AUTO_CREATE
        )
    }

    private fun observeViewModel() {
        observeFlow(viewModel.calculatedTime) {
            timerBinder?.setTime(it.toLong())
        }
        observeFlow(viewModel.selectedType) {
            timerBinder?.setType(it)
        }
        observeFlow(viewModel.selectedType) {
            binding.textCookTitle.text = when (it) {
                SOFT_TYPE -> getString(R.string.cook_eggs_soft)
                MEDIUM_TYPE -> getString(R.string.cook_eggs_medium)
                else -> getString(R.string.cook_eggs_hard)
            }
        }
        observeFlow(viewModel.calculatedTime) {
            binding.textTime.text = it.toTimerString()
            binding.viewTimer.setTimerText(it.toTimerString())
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
        binding.buttonControl.setState(ButtonState.STATE_IDLE)
        binding.viewTimer.dropProgress(FINISH_ANIMATION_DELAY)
        context?.showToast(getString(R.string.toast_finish_text))
        VibratorManager(context).makeDefaultVibration()
        binding.viewConfetti.makeDefaultConfetti()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireContext().unbindService(connection)
    }

    override fun onDetach() {
        backPressedCallback.isEnabled = false
        super.onDetach()
    }

    private companion object {
        const val FINISH_ANIMATION_DELAY = 200L
    }
}