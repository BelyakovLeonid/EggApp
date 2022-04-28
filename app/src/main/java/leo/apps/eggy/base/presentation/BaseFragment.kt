package leo.apps.eggy.base.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import leo.apps.eggy.base.utils.registerSystemInsetsListener
import javax.inject.Inject

abstract class BaseFragment(layoutId: Int) : Fragment(layoutId) {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected abstract val screenName: String
    protected abstract val viewModel: BaseViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupInsets()
    }

    override fun onResume() {
        super.onResume()
        viewModel.trackScreen(javaClass, screenName)
    }

    protected open fun setupInsets() {
        view?.registerSystemInsetsListener { v, insets, _, paddings ->
            v.updatePadding(
                top = paddings.top + insets.top,
                bottom = paddings.bottom + insets.bottom,
            )
        }
    }
}