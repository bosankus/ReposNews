package bose.ankush.reposnews.view.fragment


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.data.model.Article
import bose.ankush.reposnews.data.model.Weather
import bose.ankush.reposnews.data.news_repo.NewsRepository
import bose.ankush.reposnews.data.weather_repo.WeatherRepository
import bose.ankush.reposnews.util.ResultData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private var _topHeadLines = MutableStateFlow<ResultData<List<Article>>>(ResultData.DoNothing)
    val topHeadlines = _topHeadLines.asStateFlow()

    private var _newsData = MutableStateFlow<ResultData<List<NewsEntity?>>>(ResultData.DoNothing)
    val newsData = _newsData.asStateFlow()

    private val networkExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _newsData.value = ResultData.Failed("${exception.message}")
    }

    init {
        getWeatherReport()
        getTopHeadlines()
    }

    // fetch weather report and temperature
    private fun getWeatherReport() {
        _weatherFlow.value = ResultData.Loading
        viewModelScope.launch(networkExceptionHandler) {
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
        _topHeadLines.value = ResultData.Loading
        viewModelScope.launch(networkExceptionHandler) {
            try {
                newsSource.getHeadlines()
                    .collect { headlines ->
                        if (headlines?.articles?.isNotEmpty() == true)
                            _topHeadLines.value = ResultData.Success(headlines.articles)
                        else _topHeadLines.value = ResultData.Failed("No headlines fetched")
                    }
            } catch (e: Exception) {
                _topHeadLines.value = ResultData.Failed(e.message)
            }
        }
    }


    // fetch paginated news from local via flow.
    fun getNews(): Flow<PagingData<NewsEntity>> {
        return newsSource.getNewsFromLocal().cachedIn(viewModelScope)
    }


    // book mark a news item
    fun bookmarkNewsItem(news: NewsEntity?) {
        news?.let { viewModelScope.launch(networkExceptionHandler) { newsSource.bookmarkNewsItem(it) } }
    }
}