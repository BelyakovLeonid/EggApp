package com.example.eggyapp.ui.welcome

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.eggyapp.R
import com.example.eggyapp.databinding.FWelcomeBinding

class WelcomeFragment : Fragment(R.layout.f_welcome) {

    private val binding by viewBinding(FWelcomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonControl.setOnClickListener {
            findNavController().navigate(R.id.actionToSetupScreen)
        }
    }
}