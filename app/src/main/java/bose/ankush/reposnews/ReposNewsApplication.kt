package bose.ankush.reposnews

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.multidex.BuildConfig
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

/**Created by
Author: Ankush Bose
Date: 19,May,2021
 **/

@HiltAndroidApp
class ReposNewsApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()
}