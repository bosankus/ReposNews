package bose.ankush.reposnews.data.network

import bose.ankush.reposnews.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

/**Created by
Author: Ankush Bose
Date: 19,May,2021
 **/

interface ApiService {

    @GET("v2/everything")
    suspend fun getNews(
        @Query("q") keyword: String,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): News?
}