package esprims.gi2.ma_pharmacie.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyPharmacyApplication :Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: MyPharmacyApplication
            private set
    }
}
