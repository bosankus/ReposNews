package bose.ankush.reposnews.viewmodel


import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bose.ankush.reposnews.data.NewsRepository
import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.util.ResultData
import bose.ankush.reposnews.util.bothListsMatch
import bose.ankush.reposnews.util.convertToNewsEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**Created by
Author: Ankush Bose
Date: 19,May,2021
 **/

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataSource: NewsRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private var _newsData = MutableLiveData<ResultData<List<NewsEntity?>>>(ResultData.DoNothing)
    val newsData: LiveData<ResultData<List<NewsEntity?>>> get() = _newsData

    private var _isFreshNewsAvailable = MutableLiveData<Boolean>()
    val isFreshNewsAvailable: LiveData<Boolean> get() = _isFreshNewsAvailable

    private val newsExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _newsData.postValue(ResultData.Failed("${exception.message}"))
    }


    init {
        getNewsFromRemoteAndUpdateRoom()
        getNewsFromLocal()
    }


    // fetches news from local via flow
    private fun getNewsFromLocal() {
        _newsData.postValue(ResultData.Loading)
        viewModelScope.launch(newsExceptionHandler) {
            dataSource.getNewsFromLocal().collect { newsList ->
                if (newsList.isNotEmpty()) _newsData.postValue(ResultData.Success(newsList))
            }
        }
    }


    fun getNewsFromRemote() = viewModelScope.launch(newsExceptionHandler) {
        dataSource.getNewsFromRemote()?.articles?.convertToNewsEntity()?.toList()
    }


    fun compareNewsAndUpdateRoom() {}


    // fetch news from remote
    fun getNewsFromRemoteAndUpdateRoom() =
        viewModelScope.launch(newsExceptionHandler) {
            val savedNews: List<NewsEntity?>? =
                if (_newsData.value is ResultData.Success)
                    (_newsData.value as ResultData.Success<List<NewsEntity?>>).data
                else null
            val updatedNews: List<NewsEntity>? =
                dataSource.getNewsFromRemote()?.articles?.convertToNewsEntity()?.toList()

            updateFreshNewsToLocal(old = savedNews, new = updatedNews)
        }


    // save fresh juicy news inside room db
    private fun updateFreshNewsToLocal(old: List<NewsEntity?>?, new: List<NewsEntity>?) =
        viewModelScope.launch {
            if ((old != null && new != null) && !bothListsMatch(old, new)) {
                _isFreshNewsAvailable.postValue(true)
                dataSource.updateLocalWithUpdatedNews(new)
            } else if (old.isNullOrEmpty() && new != null) dataSource.updateLocalWithUpdatedNews(new)
            else return@launch
        }
}

/**
 * WHE APP IS LOADED FOR FIRST TIME
 * get the updated news from server
 * save the updated news in room db, which will be fetched to UI via Flow.
 * change the first time open flag to false
 */


/**
 * WHEN APP IS LOADED FROM SECOND TIME
 * get saved news from local and show in UI
 * get the updated news from server
 * check if app opened for first time, if yes, raise a flag if the updates news is new. Flag will show a button in UI
 * onClick button will save the updated news in room db, which will be fetched to UI via Flow.
 */
