package leo.apps.eggy.cook.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import leo.apps.eggy.R

class ExitDialog : DialogFragment() {

    var onConfirmListener: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context)
            .setMessage(R.string.cancel_timer_alert)
            .setNegativeButton(R.string.no) { _, _ -> dismiss() }
            .setPositiveButton(R.string.yes) { _, _ ->
                onConfirmListener?.invoke()
                dismiss()
            }
            .create()
    }
}