package bose.ankush.reposnews.data.weather_repo

import bose.ankush.reposnews.data.local.model.Weather
import bose.ankush.reposnews.data.network.WeatherApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApiService: WeatherApiService
): WeatherRepository {

    /** Getting the weather report via flow */
    override suspend fun getWeatherReport(): Flow<Weather?> = flow {
        val result: Weather? = weatherApiService.getCurrentTemperature()
        if (result != null && result.main?.temp != null) emit(result)
        else emit(null)
    }.flowOn(Dispatchers.IO)
}