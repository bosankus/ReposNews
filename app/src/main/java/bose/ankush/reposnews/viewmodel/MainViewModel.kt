package bose.ankush.reposnews.viewmodel


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
class MainViewModel @Inject constructor(private val dataSource: NewsRepository) : ViewModel() {

    private var _newsData = MutableLiveData<ResultData<List<NewsEntity?>>>(ResultData.DoNothing)
    val newsData: LiveData<ResultData<List<NewsEntity?>>> get() = _newsData

    private val newsExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _newsData.postValue(ResultData.Failed("${exception.message}"))
    }


    init {
        updateNewsFromRemote()
    }

    // fetch news from remote and store in room
    fun updateNewsFromRemote() =
        viewModelScope.launch(newsExceptionHandler) { dataSource.fetchNewsAndSaveToLocal() }


    // only fetches news from local via flow
    fun getNews() {
        _newsData.postValue(ResultData.Loading)
        viewModelScope.launch {
            dataSource.getNewsFromLocal().collect { newsList ->
                if (newsList.isNotEmpty()) _newsData.postValue(ResultData.Success(newsList))
            }
        }
    }
}