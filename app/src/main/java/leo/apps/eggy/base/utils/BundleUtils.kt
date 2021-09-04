package leo.apps.eggy.base.utils

import android.os.Bundle

fun Map<String, String>.toBundle(): Bundle {
    return Bundle(size).apply {
        this@toBundle.forEach { (key, value) ->
            putString(key, value)
        }
    }
}