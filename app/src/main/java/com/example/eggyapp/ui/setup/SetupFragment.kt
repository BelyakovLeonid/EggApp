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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleView()
        observeViewModel()
    }

    private fun handleView() {
        button_start.setOnClickListener {
            viewModel.onStartClicked()
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

    private fun observeViewModel() {
        with(viewModel) {
            observeLiveData(calculatedTime) {
                text_time.text = it?.toTimerString() ?: getString(R.string.time_empty)
            }
            observeLiveData(openCookScreen) {
                findNavController().navigate(R.id.actionToCookScreen)
            }
        }
    }
}

interface Identifiable {
    val id: Int
}

enum class SetupTemperature(override val id: Int) : Identifiable {
    FRIDGE_TEMPERATURE(0), ROOM_TEMPERATURE(1)
}

enum class SetupSize(override val id: Int) : Identifiable {
    SIZE_S(0), SIZE_M(1), SIZE_L(2)
}

enum class SetupType(override val id: Int) : Identifiable {
    SOFT_TYPE(0), MEDIUM_TYPE(1), HARD_TYPE(2)
}