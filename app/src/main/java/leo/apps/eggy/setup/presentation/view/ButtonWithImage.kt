package leo.apps.eggy.setup.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.use
import by.kirich1409.viewbindingdelegate.viewBinding
import leo.apps.eggy.R
import leo.apps.eggy.databinding.VEggTypeBinding

class ButtonWithImage @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), CheckableListenable {

    private var listener: CheckedChangedListener? = null
    override var index: Int = 0

    private var checkedState = false
        set(value) {
            if (value != field) {
                field = value
                listener?.onCheckedChanged(this, index, value)
                handleCheckedState()
            }
        }

    private val binding by viewBinding(VEggTypeBinding::bind)

    init {
        View.inflate(context, R.layout.v_egg_type, this)
        isClickable = true
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ButtonWithImage,
            defStyleAttr, 0
        ).use { a ->
            val stringResId = a.getResourceId(R.styleable.ButtonWithImage_text, 0)
            val imageResId = a.getResourceId(R.styleable.ButtonWithImage_image, 0)
            binding.textEggType.text = resources.getText(stringResId)
            binding.imageEggType.setImageResource(imageResId)
        }
    }

    override fun addOnCheckListener(listener: CheckedChangedListener) {
        this.listener = listener
    }

    override fun isChecked(): Boolean = checkedState

    override fun toggle() {
        checkedState = !checkedState
    }

    override fun setChecked(checked: Boolean) {
        checkedState = checked
    }

    override fun performClick(): Boolean {
        toggle()
        return super.performClick()
    }

    private fun handleCheckedState() {
        binding.viewTypeBackground.isActivated = checkedState
        binding.textEggType.isActivated = checkedState
    }
}