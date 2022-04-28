package leo.apps.eggy.cook.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import leo.apps.eggy.R
import leo.apps.eggy.base.presentation.BaseFragment
import leo.apps.eggy.base.presentation.vibrator.VibratorManager
import leo.apps.eggy.base.utils.getInjector
import leo.apps.eggy.base.utils.makeDefaultConfetti
import leo.apps.eggy.base.utils.observeFlow
import leo.apps.eggy.base.utils.registerSystemInsetsListener
import leo.apps.eggy.base.utils.showToast
import leo.apps.eggy.base.utils.updateMargins
import leo.apps.eggy.cook.presentation.model.CookNavigationCommand
import leo.apps.eggy.cook.presentation.model.CookSideEffect
import leo.apps.eggy.databinding.FEggCookBinding
import leo.apps.eggy.timer.TimerService

class CookFragment : BaseFragment(R.layout.f_egg_cook), View.OnClickListener {

    private val viewModel: CookViewModel by viewModels { viewModelFactory }
    private val binding by viewBinding(FEggCookBinding::bind)

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.onBackPressed()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getInjector().inject(this)
        requireActivity().onBackPressedDispatcher.addCallback(backPressedCallback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleView()
        observeViewModel()
        requireContext().bindService(
            Intent(requireContext(), TimerService::class.java),
            viewModel.serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun setupInsets() {
        binding.buttonBack.registerSystemInsetsListener { v, insets, margins, _ ->
            v.updateMargins(top = margins.top + insets.top)
        }
        binding.textCookTitle.registerSystemInsetsListener { v, insets, margins, _ ->
            v.updateMargins(top = margins.top + insets.top)
        }
        binding.root.registerSystemInsetsListener { v, insets, _, paddings ->
            v.updatePadding(bottom = paddings.bottom + insets.bottom)
        }
    }

    override fun onClick(v: View?) = when (v?.id) {
        binding.buttonBack.id -> viewModel.onBackPressed()
        binding.buttonControl.id -> viewModel.onControlClick()
        else -> throw NotImplementedError()
    }

    private fun observeViewModel() {
        observeFlow(viewModel.state) { state ->
            binding.textCookTitle.setText(state.titleTextId)
            binding.textTime.text = state.boiledTimeText
            binding.viewTimer.setProgress(state.progress)
            binding.viewTimer.setTimerText(state.timerText)
            binding.buttonControl.setText(state.buttonTextId)
        }
        observeFlow(viewModel.sideEffects) { effect ->
            when (effect) {
                is CookSideEffect.Finish -> showFinish()
                is CookSideEffect.Cancel -> binding.viewTimer.dropProgress()
            }
        }
        observeFlow(viewModel.navigationCommands) { command ->
            when (command) {
                is CookNavigationCommand.PopUp -> findNavController().navigateUp()
                is CookNavigationCommand.ShowExitDialog -> ExitDialog().show(childFragmentManager, null)
            }
        }
    }

    private fun handleView() {
        binding.buttonControl.setOnClickListener(this)
        binding.buttonBack.setOnClickListener(this)
    }

    private fun showFinish() {
        binding.viewTimer.dropProgress(FINISH_ANIMATION_DELAY)
        context?.showToast(getString(R.string.toast_finish_text))
        VibratorManager(context).makeDefaultVibration()
        binding.viewConfetti.makeDefaultConfetti()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireContext().unbindService(viewModel.serviceConnection)
        viewModel.onUnbindService()
    }

    override fun onDetach() {
        backPressedCallback.isEnabled = false
        super.onDetach()
    }

    private companion object {
        const val FINISH_ANIMATION_DELAY = 200L
    }
}