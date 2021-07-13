package com.example.eggyapp.ui.base

import android.os.Bundle
import android.view.View
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.eggyapp.base.utils.registerSystemInsetsListener
import javax.inject.Inject

open class BaseFragment(layoutId: Int) : Fragment(layoutId) {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.registerSystemInsetsListener { v, insets, _, paddings ->
            v.updatePadding(
                top = paddings.top + insets.top,
                bottom = paddings.bottom + insets.bottom,
            )
        }
    }
}