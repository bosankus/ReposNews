package bose.ankush.reposnews.viewmodel


import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bose.ankush.reposnews.data.NewsRepository
import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.util.ResultData
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

    private var freshNews = listOf<NewsEntity?>()

    init {
        getNewsFromLocal()
        updateFreshNewsFromRemote()
    }


    // fetches news from local via flow
    private fun getNewsFromLocal() {
        _newsData.postValue(ResultData.Loading)
        viewModelScope.launch(newsExceptionHandler) {
            dataSource.getNewsFromLocal()?.collect { newsList ->
                freshNews = newsList
                if (newsList.isNotEmpty()) _newsData.postValue(ResultData.Success(newsList))
            }
                ?: _newsData.postValue(ResultData.Failed("Oops! No news found."))
        }
    }

    // update fresh news from remote
    fun updateFreshNewsFromRemote() =
        viewModelScope.launch(newsExceptionHandler) { dataSource.updateNews() }
}
