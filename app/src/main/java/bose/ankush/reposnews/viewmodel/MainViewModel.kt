package bose.ankush.reposnews.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bose.ankush.reposnews.data.NewsRepository
import bose.ankush.reposnews.model.News
import bose.ankush.reposnews.util.ResultData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 7/18/2020, 4:01 PM
 */

@HiltViewModel
class MainViewModel @Inject constructor(private val dataSource: NewsRepository) :
    ViewModel() {

    private var _newsData = MutableLiveData<ResultData<News>>(ResultData.Loading)
    val newsData: LiveData<ResultData<News>> get() = _newsData

    private val newsExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _newsData.postValue(ResultData.Failed("${exception.message}"))
    }

    init {
        fetchNewsFromNetwork()
    }

    private fun fetchNewsFromNetwork(keyword: String = "covid-19") {
        viewModelScope.launch(newsExceptionHandler) {
            val response = dataSource.fetchNews(keyword)
            response?.let { _newsData.postValue(ResultData.Success(it)) }
                ?: _newsData.postValue(ResultData.Failed())
        }
    }
}