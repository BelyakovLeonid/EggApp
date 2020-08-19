package com.example.eggyapp.ui.setup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eggyapp.R
import com.example.eggyapp.utils.findById
import com.example.eggyapp.utils.observeLiveData
import com.example.eggyapp.utils.toTimerString
import kotlinx.android.synthetic.main.f_egg_setup.*

class SetupFragment : Fragment(R.layout.f_egg_setup) {

    private val viewModel: SetupViewModel by viewModels()

    private fun observeViewModel() {
        with(viewModel) {
            observeLiveData(calculatedTime) {
                text_time.text = it.toTimerString()
            }
            observeLiveData(isCookEnable) {
                button_start.isEnabled = it
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        handleView()
    }

    private fun handleView() {
        button_start.setOnClickListener {
            findNavController().navigate(R.id.actionToCookScreen)
        }
        group_temperature_buttons.onCheckedIndexListener = {
            viewModel.onSelectTemperature(findById(it))
        }
        group_size_buttons.onCheckedIndexListener = {
            viewModel.onSelectSize(findById(it))
        }
        group_type_buttons.onCheckedIndexListener = {
            viewModel.onSelectType(findById(it))
        }
    }
}