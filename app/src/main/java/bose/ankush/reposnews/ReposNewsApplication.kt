package bose.ankush.reposnews

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


/**Created by
Author: Ankush Bose
Date: 19,May,2021
 **/

@HiltAndroidApp
class ReposNewsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}