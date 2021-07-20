package leo.apps.eggy.setup.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import leo.apps.eggy.R
import leo.apps.eggy.base.data.model.SetupSize
import leo.apps.eggy.base.data.model.SetupTemperature
import leo.apps.eggy.base.data.model.SetupType
import leo.apps.eggy.base.presentation.BaseFragment
import leo.apps.eggy.base.utils.getInjector
import leo.apps.eggy.base.utils.observeFlow
import leo.apps.eggy.databinding.FEggSetupBinding
import leo.apps.eggy.setup.presentation.view.CheckedIndexListener

class SetupFragment : BaseFragment(R.layout.f_egg_setup), CheckedIndexListener {

    private val viewModel: SetupViewModel by viewModels { viewModelFactory }
    private val binding by viewBinding(FEggSetupBinding::bind)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getInjector().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        handleView()
    }

    override fun onCheckedIndex(view: View, index: Int) {
        when (view.id) {
            binding.groupTemperatureButtons.id -> viewModel.onSelectTemperatureIndex(index)
            binding.groupSizeButtons.id -> viewModel.onSelectSizeIndex(index)
            binding.groupTypeButtons.id -> viewModel.onSelectTypeIndex(index)
        }
    }

    private fun observeViewModel() {
        observeFlow(viewModel.state) { state ->
            binding.textTime.setTime(state.calculatedTime)
            binding.groupTemperatureButtons.setSelectedItem(state.selectedTemperatureIndex)
            binding.groupSizeButtons.setSelectedItem(state.selectedSizeIndex)
            binding.groupTypeButtons.setSelectedItem(state.selectedTypeIndex)
            binding.buttonStart.isEnabled = state.isButtonNextEnable
        }
    }

    private fun handleView() {
        binding.groupTemperatureButtons.setOnCheckedIndexListener(this)
        binding.groupSizeButtons.setOnCheckedIndexListener(this)
        binding.groupTypeButtons.setOnCheckedIndexListener(this)
        binding.buttonStart.setOnClickListener {
            findNavController().navigate(R.id.actionToCookScreen)
        }
    }
}