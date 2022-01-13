package bose.ankush.reposnews.data.network

import bose.ankush.reposnews.BuildConfig
import bose.ankush.reposnews.util.HEADLINE_SOURCE_NAME
import retrofit2.http.GET
import retrofit2.http.Query

/**Created by
Author: Ankush Bose
Date: 19,May,2021
 **/

// Get your key from "newapi.org" and assign it to API_KEU in local.properties file

interface ApiService {

    @GET("v2/top-headlines")
    suspend fun getHeadlines(
        @Query("sources") sources: String = HEADLINE_SOURCE_NAME,
        @Query("apikey") apiKey: String = BuildConfig.API_KEY
    ): News?


    @GET("v2/everything")
    suspend fun getNews(
        @Query("q") keyword: String,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): News?
}