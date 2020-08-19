package com.example.eggyapp.ui.base

import androidx.fragment.app.Fragment
import com.example.eggyapp.di.ViewModelFactory
import javax.inject.Inject

open class BaseFragment(layoutId: Int) : Fragment(layoutId) {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
}