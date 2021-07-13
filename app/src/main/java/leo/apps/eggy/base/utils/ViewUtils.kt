package leo.apps.eggy.base.utils

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.Insets
import androidx.core.view.*

fun View.dpToPx(dp: Int) = resources.displayMetrics.density * dp

val Toast?.isShowing
    get() = this?.view?.isShown == true


fun View.registerSystemInsetsListener(
    listener: (view: View, insets: Insets, margins: Rect, paddings: Rect) -> Unit
) {
    this.registerInsetsListener(
        WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime(),
        listener
    )
}

fun View.registerInsetsListener(
    insetsType: Int,
    listener: (view: View, insets: Insets, margins: Rect, paddings: Rect) -> Unit
) {
    val defaultPaddings = Rect(paddingLeft, paddingTop, paddingRight, paddingEnd)
    val defaultMargins = Rect(marginLeft, marginTop, marginRight, marginEnd)
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val tappableInsets = insets.getInsets(insetsType)
        listener.invoke(v, tappableInsets, defaultMargins, defaultPaddings)
        insets
    }
    ViewCompat.requestApplyInsets(this)
}