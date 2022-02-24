package bose.ankush.reposnews.data.network

import bose.ankush.reposnews.BuildConfig
import bose.ankush.reposnews.data.local.model.TopHeadlinesIndia
import bose.ankush.reposnews.util.COUNTRY_NAME
import retrofit2.http.GET
import retrofit2.http.Query

/**Created by
Author: Ankush Bose
Date: 19,May,2021
 **/

// Get your key from "newapi.org" and assign it to API_KEU in local.properties file

interface NewsApiService {

    /*for fetching news and headlines*/
    @GET("v2/top-headlines")
    suspend fun getTopHeadlinesIndia(
        @Query("country") sources: String = COUNTRY_NAME,
        @Query("apikey") apiKey: String = BuildConfig.NEWS_KEY
    ): TopHeadlinesIndia?

    @GET("v2/everything")
    suspend fun getNews(
        @Query("q") keyword: String,
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_KEY
    ): News?
}