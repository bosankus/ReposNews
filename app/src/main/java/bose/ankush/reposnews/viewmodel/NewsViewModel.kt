package bose.ankush.reposnews.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bose.ankush.reposnews.data.NewsRepository
import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.data.local.TopHeadlinesIndia
import bose.ankush.reposnews.util.ResultData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

/**Created by
Author: Ankush Bose
Date: 19,May,2021
 **/

@HiltViewModel
class NewsViewModel @Inject constructor(private val dataSource: NewsRepository) : ViewModel() {

    private var _topHeadLines = MutableLiveData<ResultData<TopHeadlinesIndia>>(ResultData.DoNothing)
    val topHeadlines: LiveData<ResultData<TopHeadlinesIndia>> = _topHeadLines

    private var _newsData = MutableLiveData<ResultData<List<NewsEntity?>>>(ResultData.DoNothing)
    val newsData: LiveData<ResultData<List<NewsEntity?>>> get() = _newsData

    private var _newsUpdateStatus = MutableLiveData<Boolean>()
    val newsUpdateStatus: LiveData<Boolean> get() = _newsUpdateStatus

    private val newsExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _newsData.postValue(ResultData.Failed("${exception.message}"))
    }

    private var freshNews = listOf<NewsEntity?>()


    private val sortFlow = MutableStateFlow(SORT_TYPE.DEFAULT)

    init {
        getNewsFromLocal()
        updateFreshNewsFromRemote()
    }


    // fetches top headlines of India via flow
    private fun getTopHeadlines() {
        _topHeadLines.postValue(ResultData.Loading)
        viewModelScope.launch(newsExceptionHandler) {
            dataSource.getHeadlines().collect { headlines ->
                headlines?.let { _topHeadLines.postValue(ResultData.Success(it)) }
                    ?: _topHeadLines.postValue(ResultData.Failed("No headlines fetched"))
            }
        }
    }


    // fetches news from local via flow.
    // if news list is empty, calls [updateFreshNewsFromRemote()] method.
    private fun getNewsFromLocal() {
        _newsData.postValue(ResultData.Loading)
        viewModelScope.launch(newsExceptionHandler) {
            dataSource.getNewsFromLocal()?.collect { newsList ->
                freshNews = newsList
                if (newsList.isNotEmpty())
                    _newsData.postValue(ResultData.Success(newsList))
                else updateFreshNewsFromRemote()
            }
        }
    }


    // update fresh news from remote and saves in room db
    fun updateFreshNewsFromRemote() =
        viewModelScope.launch(newsExceptionHandler) {
            _newsUpdateStatus.postValue(dataSource.updateNews())
        }


    // book mark a news item
    fun bookmarkNewsItem(news: NewsEntity?) {
        news?.let { viewModelScope.launch { dataSource.bookmarkNewsItem(it) } }
    }


    // get all bookmarked news item
    fun getAllBookmarkedNews() {
        _newsData.postValue(ResultData.Loading)
        viewModelScope.launch {
            dataSource.getAllBookmarkedNews()?.collect { newsList ->
                if (newsList.isNotEmpty()) _newsData.postValue(ResultData.Success(newsList))
                else _newsData.postValue(ResultData.Failed("No news found"))
            }
        }
    }


    // Using Flow combine
    val allNews: Flow<List<NewsEntity?>> = checkNotNull(dataSource.getNewsFromLocal())
    val allBookmarkedNews: Flow<List<NewsEntity?>> = checkNotNull(dataSource.getAllBookmarkedNews())

    val combinedNewsFlow = combine(allNews, allBookmarkedNews) { a, b ->
        a + b
    }

}

enum class SORT_TYPE {
    DEFAULT, BOOKMARKED
}
