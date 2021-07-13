package leo.apps.eggy.setup.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import leo.apps.eggy.R
import leo.apps.eggy.databinding.FEggSetupBinding
import leo.apps.eggy.base.EggApp
import leo.apps.eggy.base.data.model.SetupSize
import leo.apps.eggy.base.data.model.SetupTemperature
import leo.apps.eggy.base.data.model.SetupType
import leo.apps.eggy.base.presentation.BaseFragment
import leo.apps.eggy.base.utils.observeFlow

class SetupFragment : BaseFragment(R.layout.f_egg_setup) {

    private val viewModel: SetupViewModel by viewModels { viewModelFactory }
    private val binding by viewBinding(FEggSetupBinding::bind)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as EggApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        handleView()
    }

    private fun observeViewModel() {
        observeFlow(viewModel.calculatedTime) {
            binding.textTime.setTime(it)
        }
        observeFlow(viewModel.selectedTemperature) {
            binding.groupTemperatureButtons.setSelectedItem(SetupTemperature.values().indexOf(it))
        }
        observeFlow(viewModel.selectedSize) {
            binding.groupSizeButtons.setSelectedItem(SetupSize.values().indexOf(it))
        }
        observeFlow(viewModel.selectedType) {
            binding.groupTypeButtons.setSelectedItem(SetupType.values().indexOf(it))
        }
        observeFlow(viewModel.isCookEnable) {
            binding.buttonStart.isEnabled = it
        }
    }

    private fun handleView() {
        binding.buttonStart.setOnClickListener {
            findNavController().navigate(R.id.actionToCookScreen)
        }
        binding.groupTemperatureButtons.onCheckedIndexListener = {
            viewModel.onSelectTemperature(SetupTemperature.values().get(it))
        }
        binding.groupSizeButtons.onCheckedIndexListener = {
            viewModel.onSelectSize(SetupSize.values().get(it))
        }
        binding.groupTypeButtons.onCheckedIndexListener = {
            viewModel.onSelectType(SetupType.values().get(it))
        }
    }
}