package bose.ankush.reposnews.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import bose.ankush.reposnews.data.NewsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**Created by
Author: Ankush Bose
Date: 22,May,2021
 **/

@HiltWorker
class NewsUpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val dataSource: NewsRepository
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        return try {
            dataSource.updateNewsFromInternet()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}

