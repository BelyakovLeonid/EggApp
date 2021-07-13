package leo.apps.eggy.base.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast

fun Context.showToast(string: String): Toast =
    Toast.makeText(this, string, Toast.LENGTH_LONG).apply {
        show()
    }

fun Context.getBitmap(bitmapId: Int?): Bitmap =
    BitmapFactory.decodeResource(resources, bitmapId ?: 0)


