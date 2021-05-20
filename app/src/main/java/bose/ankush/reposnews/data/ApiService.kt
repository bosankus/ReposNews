package bose.ankush.reposnews.data

import bose.ankush.reposnews.model.News
import bose.ankush.reposnews.util.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 7/18/2020, 5:55 PM
 */
interface ApiService {

    @GET("/v2/everything")
    suspend fun getNews(
        @Query("q") keyword: String,
        @Query("apiKey") apiKey: String = API_KEY
    ): News?

}