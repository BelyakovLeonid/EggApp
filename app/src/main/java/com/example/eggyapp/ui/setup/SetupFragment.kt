package com.example.eggyapp.ui.setup

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eggyapp.EggApp
import com.example.eggyapp.R
import com.example.eggyapp.ui.base.BaseFragment
import com.example.eggyapp.utils.findById
import com.example.eggyapp.utils.observeLiveData
import kotlinx.android.synthetic.main.f_egg_setup.*

class SetupFragment : BaseFragment(R.layout.f_egg_setup) {

    private val viewModel: SetupViewModel by viewModels { viewModelFactory }

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
                textTime.setTime(it)
            }
            observeLiveData(selectedTemperature) {
                groupTemperatureButtons.setSelectedItem(it.id)
            }
            observeLiveData(selectedSize) {
                groupSizeButtons.setSelectedItem(it.id)
            }
            observeLiveData(selectedType) {
                groupTypeButtons.setSelectedItem(it.id)
            }

            observeLiveData(isCookEnable) {
                buttonStart.isEnabled = it
            }
        }
    }

    private fun handleView() {
        buttonStart.setOnClickListener {
            findNavController().navigate(R.id.actionToCookScreen)
        }
        groupTemperatureButtons.onCheckedIndexListener = {
            viewModel.onSelectTemperature(findById(it))
        }
        groupSizeButtons.onCheckedIndexListener = {
            viewModel.onSelectSize(findById(it))
        }
        groupTypeButtons.onCheckedIndexListener = {
            viewModel.onSelectType(findById(it))
        }
    }
}