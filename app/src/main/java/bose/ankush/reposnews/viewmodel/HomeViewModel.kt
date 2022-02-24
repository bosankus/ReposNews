package bose.ankush.reposnews.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.data.local.model.Article
import bose.ankush.reposnews.data.local.model.Weather
import bose.ankush.reposnews.data.news_repo.NewsRepository
import bose.ankush.reposnews.data.weather_repo.WeatherRepository
import bose.ankush.reposnews.util.ResultData
import bose.ankush.reposnews.util.logMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**Created by
Author: Ankush Bose
Date: 19,May,2021
 **/

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsSource: NewsRepository,
    private val weatherSource: WeatherRepository
) : ViewModel() {

    private var _weatherFlow =
        MutableStateFlow<ResultData<Weather>>(ResultData.DoNothing)
    val weatherFlow = _weatherFlow.asStateFlow()

    private var _topHeadLines = MutableLiveData<ResultData<List<Article>>>(ResultData.DoNothing)
    val topHeadlines: LiveData<ResultData<List<Article>>> = _topHeadLines

    private var _newsData = MutableLiveData<ResultData<List<NewsEntity?>>>(ResultData.DoNothing)
    val newsData: LiveData<ResultData<List<NewsEntity?>>> get() = _newsData

    private var _newsUpdateStatus = MutableLiveData<Boolean>()
    val newsUpdateStatus: LiveData<Boolean> get() = _newsUpdateStatus

    private val newsExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _newsData.postValue(ResultData.Failed("${exception.message}"))
    }

    private var freshNews = listOf<NewsEntity?>()

    init {
        getWeatherReport()
        getTopHeadlines()
        getNewsFromLocal()
        updateFreshNewsFromRemote()
    }

    // fetch weather report and temperature
    private fun getWeatherReport() {
        _weatherFlow.value = ResultData.Loading
        viewModelScope.launch {
            try {
                weatherSource.getWeatherReport().collect { weather ->
                    _weatherFlow.value = weather?.let { ResultData.Success(it) }
                        ?: ResultData.Failed("Something went wrong!")
                }
            } catch (e: Exception) {
                _weatherFlow.value = ResultData.Failed(e.message)
            }
        }
    }


    // fetch top headlines of India via flow
    private fun getTopHeadlines() {
        _topHeadLines.postValue(ResultData.Loading)
        viewModelScope.launch(newsExceptionHandler) {
            try {
                newsSource.getHeadlines()
                    .collect { headlines ->
                        if (headlines?.articles?.isNotEmpty() == true)
                            _topHeadLines.postValue(ResultData.Success(headlines.articles))
                        else _topHeadLines.postValue(ResultData.Failed("No headlines fetched"))
                    }
            } catch (e: Exception) {
                logMessage(e.message.toString())
            }
        }
    }


    // fetch news from local via flow.
    // if news list is empty, calls [updateFreshNewsFromRemote()] method.
    private fun getNewsFromLocal() {
        _newsData.postValue(ResultData.Loading)
        viewModelScope.launch(newsExceptionHandler) {
            newsSource.getNewsFromLocal()?.collect { newsList ->
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
            _newsUpdateStatus.postValue(newsSource.updateNews())
        }


    // book mark a news item
    fun bookmarkNewsItem(news: NewsEntity?) {
        news?.let { viewModelScope.launch { newsSource.bookmarkNewsItem(it) } }
    }


    // get all bookmarked news item
    /*fun getAllBookmarkedNews() {
        _newsData.postValue(ResultData.Loading)
        viewModelScope.launch {
            newsSource.getAllBookmarkedNews()?.collect { newsList ->
                if (newsList.isNotEmpty()) _newsData.postValue(ResultData.Success(newsList))
                else _newsData.postValue(ResultData.Failed("No news found"))
            }
        }
    }*/

}