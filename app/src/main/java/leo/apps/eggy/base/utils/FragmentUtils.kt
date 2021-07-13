package leo.apps.eggy.base.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import leo.apps.eggy.base.EggApp

fun Fragment.getInjector() = (requireActivity().application as EggApp).appComponent

fun <T> Fragment.observeFlow(flow: Flow<T>, action: (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        flow.collect { action(it) }
    }
}