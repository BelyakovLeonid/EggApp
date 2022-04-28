package leo.apps.eggy.base.utils

import android.app.Activity
import leo.apps.eggy.base.EggApp

fun Activity.getInjector() = (application as EggApp).appComponent