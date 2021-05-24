package bose.ankush.reposnews.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import bose.ankush.reposnews.data.NewsRepository
import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.util.ResultData
import bose.ankush.reposnews.util.WORKER_TAG
import bose.ankush.reposnews.worker.NewsUpdateWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**Created by
Author: Ankush Bose
Date: 19,May,2021
 **/

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataSource: NewsRepository,
    private val workManager: WorkManager
) :
    ViewModel() {

    private var _newsData = MutableLiveData<ResultData<List<NewsEntity?>>>(ResultData.DoNothing)
    val newsData: LiveData<ResultData<List<NewsEntity?>>> get() = _newsData

    private val newsExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _newsData.postValue(ResultData.Failed("${exception.message}"))
    }


    init {
        startNewsUpdateWorker()
    }


    fun fetchNews() {
        _newsData.postValue(ResultData.Loading)
        viewModelScope.launch(newsExceptionHandler) {
            val response = dataSource.getNewsFromLocal()
            if (response.isEmpty()) {
                val news = dataSource.fetchNewsAndSaveToLocal()
                _newsData.postValue(ResultData.Success(news))
            } else {
                _newsData.postValue(ResultData.Success(response))
            }
        }
    }


    fun updateNews() {
        _newsData.postValue(ResultData.Loading)
        viewModelScope.launch(newsExceptionHandler) {
            dataSource.updateNewsFromInternet()
            _newsData.postValue(ResultData.Success(dataSource.getNewsFromLocal()))
        }
    }


    private fun startNewsUpdateWorker() {
        val workConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequest = PeriodicWorkRequestBuilder<NewsUpdateWorker>(2, TimeUnit.DAYS)
            .setConstraints(workConstraints)
            .addTag(WORKER_TAG)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        workManager
            .enqueueUniquePeriodicWork(
                "work_name",
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWorkRequest
            )
    }
}