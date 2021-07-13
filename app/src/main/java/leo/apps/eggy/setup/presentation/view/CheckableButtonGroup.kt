package leo.apps.eggy.setup.presentation.view

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

        val checkedIndex = getCheckedIndex()
        if (checkedIndex != lastCheckedIndex) {
            onCheckedIndexListener?.invoke(checkedIndex)
            lastCheckedIndex = checkedIndex
        }
    }

    fun setSelectedItem(id: Int) {
        iterateCheckableChildren {
            it.isChecked = it.getIndex() == id
        }
    }

    private fun getCheckedIndex(): Int {
        iterateCheckableChildren {
            if (it.isChecked) {
                return it.getIndex()
            }
        }
        return NO_ITEM_SELECTED
    }

    private inline fun iterateCheckableChildren(block: (child: CheckableListenable) -> Unit) {
        children.iterator().forEach {
            (it as? CheckableListenable)?.let(block)
        }
    }

    private companion object{
        const val NO_ITEM_SELECTED = -1
    }
}