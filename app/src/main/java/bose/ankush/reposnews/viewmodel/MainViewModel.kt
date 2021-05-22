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
import bose.ankush.reposnews.util.logMessage
import bose.ankush.reposnews.worker.NewsUpdateWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
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

    var workInfoStatus = workManager.getWorkInfosByTagLiveData(WORKER_TAG)

    fun fetchNews() {
        logMessage("again")
        _newsData.postValue(ResultData.Loading)
        viewModelScope.launch(newsExceptionHandler) {
            val response = dataSource.getNewsFromLocal()
            if (response.isEmpty()) {
                val news = dataSource.fetchNewsAndSaveToLocal()
                _newsData.postValue(ResultData.Success(news))
            } else {
                updateNews()
                _newsData.postValue(ResultData.Success(response))
            }
        }
    }


    private fun updateNews() {

        val workConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<NewsUpdateWorker>()
            .setConstraints(workConstraints)
            .addTag(WORKER_TAG)
            .build()

        workManager.enqueueUniqueWork(
            "news_update_person",
            ExistingWorkPolicy.REPLACE,
            oneTimeWorkRequest
        )
    }

    override fun onCleared() {
        super.onCleared()
        logMessage("onCleared")
    }

}