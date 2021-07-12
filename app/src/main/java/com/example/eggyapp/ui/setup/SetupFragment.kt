package com.example.eggyapp.ui.setup

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.eggyapp.EggApp
import com.example.eggyapp.R
import com.example.eggyapp.databinding.FEggSetupBinding
import com.example.eggyapp.ui.base.BaseFragment
import com.example.eggyapp.utils.findById
import com.example.eggyapp.utils.observeLiveData

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
        with(viewModel) {
            observeLiveData(calculatedTime) {
                binding.textTime.setTime(it)
            }
            observeLiveData(selectedTemperature) {
                binding.groupTemperatureButtons.setSelectedItem(it.id)
            }
            observeLiveData(selectedSize) {
                binding.groupSizeButtons.setSelectedItem(it.id)
            }
            observeLiveData(selectedType) {
                binding.groupTypeButtons.setSelectedItem(it.id)
            }
            observeLiveData(isCookEnable) {
                binding.buttonStart.isEnabled = it
            }
        }
    }

    private fun handleView() {
        binding.buttonStart.setOnClickListener {
            findNavController().navigate(R.id.actionToCookScreen)
        }
        binding.groupTemperatureButtons.onCheckedIndexListener = {
            viewModel.onSelectTemperature(findById(it))
        }
        binding.groupSizeButtons.onCheckedIndexListener = {
            viewModel.onSelectSize(findById(it))
        }
        binding.groupTypeButtons.onCheckedIndexListener = {
            viewModel.onSelectType(findById(it))
        }
    }
}