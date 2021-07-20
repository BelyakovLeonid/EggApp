package leo.apps.eggy.setup.presentation.view

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.button.MaterialButton

class CheckableMaterialButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialButton(context, attrs, defStyleAttr), CheckableListenable {

    private var listener: CheckedChangedListener? = null
    override var index: Int = 0

    init {
        isCheckable = true
    }

    override fun addOnCheckListener(listener: CheckedChangedListener) {
        this.listener = listener
    }

    override fun setChecked(checked: Boolean) {
        if (isChecked != checked) {
            super.setChecked(checked)
            listener?.onCheckedChanged(this, index, checked)
        }
    }
}