package bose.ankush.reposnews.data.weather_repo

import bose.ankush.reposnews.data.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getWeatherReport(): Flow<Weather?>
}