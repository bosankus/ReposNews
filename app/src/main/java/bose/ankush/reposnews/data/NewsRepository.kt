package bose.ankush.reposnews.data

import javax.inject.Inject

/**Created by
Author: Ankush Bose
Date: 19,May,2021
 **/

class NewsRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun fetchNews(keyword: String) = apiService.getNews(keyword)
}