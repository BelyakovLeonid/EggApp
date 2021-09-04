package leo.apps.eggy.welcome.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import leo.apps.eggy.R
import leo.apps.eggy.base.analytics.Analytics
import leo.apps.eggy.base.presentation.BaseFragment
import leo.apps.eggy.base.utils.getInjector
import leo.apps.eggy.databinding.FWelcomeBinding

class WelcomeFragment : BaseFragment(R.layout.f_welcome) {
    override val screenName = Analytics.Welcome.SCREEN_NAME
    override val viewModel: WelcomeViewModel by viewModels { viewModelFactory }
    private val binding by viewBinding(FWelcomeBinding::bind)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getInjector().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            viewModel.onButtonClick()
            findNavController().navigate(R.id.actionToSetupScreen)
        }
    }
}