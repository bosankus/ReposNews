package com.bosankus.reposnews.weather_feature.data.repository

import bose.ankush.reposnews.data.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getWeatherReport(): Flow<Weather?>
}