package leo.apps.eggy.base

import android.app.Application
import leo.apps.eggy.base.di.DaggerAppComponent

class EggApp : Application() {

    val appComponent by lazy {
        DaggerAppComponent.factory().create()
    }
}