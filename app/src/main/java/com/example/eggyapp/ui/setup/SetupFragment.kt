package com.example.eggyapp.ui.setup

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.eggyapp.EggApp
import com.example.eggyapp.R
import com.example.eggyapp.base.utils.observeFlow
import com.example.eggyapp.data.model.SetupSize
import com.example.eggyapp.data.model.SetupTemperature
import com.example.eggyapp.data.model.SetupType
import com.example.eggyapp.databinding.FEggSetupBinding
import com.example.eggyapp.ui.base.BaseFragment

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