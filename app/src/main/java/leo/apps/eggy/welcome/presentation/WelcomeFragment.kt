package leo.apps.eggy.welcome.presentation

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import leo.apps.eggy.R
import leo.apps.eggy.databinding.FWelcomeBinding
import leo.apps.eggy.base.presentation.BaseFragment

class WelcomeFragment : BaseFragment(R.layout.f_welcome) {

    private val binding by viewBinding(FWelcomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonControl.setOnClickListener {
            findNavController().navigate(R.id.actionToSetupScreen)
        }
    }
}