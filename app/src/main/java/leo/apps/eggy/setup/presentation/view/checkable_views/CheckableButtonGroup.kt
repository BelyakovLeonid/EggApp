package leo.apps.eggy.setup.presentation.view.checkable_views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.children

class CheckableButtonGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), CheckedListener {

    var onCheckedIndexListener: ((Int) -> Unit)? = null
    private var lastCheckedIndex: Int? = null
    private var itemsCount = 0

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        when (child) {
            is CheckableListenable -> {
                child.addOnCheckListener(this)
                child.setIndex(itemsCount++)
            }
        }
        super.addView(child, index, params)
    }

    override fun onCheckedChanged(view: CheckableListenable, index: Int, isChecked: Boolean) {
        if (isChecked) {
            iterateCheckableChildren {
                if (it != view) {
                    it.isChecked = false
                }
            }
        }
        getCheckedIndex().ifChange {
            onCheckedIndexListener?.invoke(it)
        }
    }

    private fun getCheckedIndex(): Int {
        iterateCheckableChildren {
            if (it.isChecked) {
                return it.getIndex()
            }
        }
        return -1
    }

    private inline fun iterateCheckableChildren(block: (child: CheckableListenable) -> Unit) {
        children.iterator().forEach {
            (it as? CheckableListenable)?.let(block)
        }
    }

    private inline fun Int.ifChange(block: (Int) -> Unit?) {
        if (this != lastCheckedIndex) {
            block.invoke(this)
            lastCheckedIndex = this
        }
    }

    fun setSelectedItem(id: Int) {
        iterateCheckableChildren {
            it.isChecked = it.getIndex() == id
        }
    }
}