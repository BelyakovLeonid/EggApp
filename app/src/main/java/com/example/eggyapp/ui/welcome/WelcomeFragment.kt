package com.example.eggyapp.ui.welcome

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.eggyapp.R
import kotlinx.android.synthetic.main.tes.*

class WelcomeFragment : Fragment(R.layout.tes) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button.setOnClickListener {
            val x = edit_digit.text.toString().toInt()
            clock.setTime(x)
        }
//        button_control.setOnClickListener {
//            findNavController().navigate(WelcomeFragmentDirections.actionToSetupScreen())
//            activity?.window?.setBackgroundDrawableResource(R.drawable.workflow_background)
//        }
    }
}