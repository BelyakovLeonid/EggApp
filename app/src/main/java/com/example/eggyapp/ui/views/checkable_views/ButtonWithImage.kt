package com.example.eggyapp.ui.views.checkable_views

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.eggyapp.R
import com.example.eggyapp.databinding.VEggTypeBinding
import kotlinx.parcelize.Parcelize


class ButtonWithImage @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), CheckableListenable {

    private var onCheckedListener: CheckedListener? = null
    private var index: Int = 0

    private var checkedState = false
        set(value) {
            if (value != field) {
                field = value
                onCheckedListener?.onCheckedChanged(this, index, value)
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
        ).apply {
            try {
                val stringResId = getResourceId(R.styleable.ButtonWithImage_text, 0)
                val imageResId = getResourceId(R.styleable.ButtonWithImage_image, 0)
                binding.textEggType.text = resources.getText(stringResId)
                binding.imageEggType.setImageResource(imageResId)
            } finally {
                recycle()
            }
        }
    }

    override fun addOnCheckListener(listener: CheckedListener) {
        onCheckedListener = listener
    }

    override fun setIndex(index: Int) {
        this.index = index
    }

    override fun getIndex() = index

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

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(null)
        checkedState = (state as SavedState).checkedState
    }

    override fun onSaveInstanceState(): Parcelable? {
        super.onSaveInstanceState()
        return SavedState(checkedState)
    }

    @Parcelize
    private data class SavedState(
        val checkedState: Boolean
    ) : Parcelable
}