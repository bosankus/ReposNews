package com.bosankus.reposnews.weather_feature.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bose.ankush.reposnews.data.model.Weather
import bose.ankush.reposnews.util.ResultData
import com.bosankus.reposnews.weather_feature.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherSource: WeatherRepository
) : ViewModel() {

    private var _weatherFlow = MutableStateFlow<ResultData<Weather>>(ResultData.DoNothing)
    val weatherFlow = _weatherFlow.asStateFlow()

    private val networkExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _weatherFlow.value = ResultData.Failed("${exception.message}")
    }


    init {
        fetchWeatherDetails()
    }


    private fun fetchWeatherDetails() {
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
}