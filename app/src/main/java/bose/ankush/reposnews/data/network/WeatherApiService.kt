package bose.ankush.reposnews.data.network

import bose.ankush.reposnews.BuildConfig
import bose.ankush.reposnews.data.local.model.Weather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    /*for fetching user's current city temperature*/
    @GET("/data/2.5/weather")
    suspend fun getCurrentTemperature(
        @Query("q") location: String = "Kolkata",
        @Query("APPID") AppId: String = BuildConfig.WEATHER_KEY
    ): Weather?
}