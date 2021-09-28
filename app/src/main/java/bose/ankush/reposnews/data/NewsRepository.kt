package bose.ankush.reposnews.data

import bose.ankush.reposnews.data.local.NewsDao
import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.data.network.ApiService
import bose.ankush.reposnews.data.network.News
import bose.ankush.reposnews.util.convertToNewsEntity
import bose.ankush.reposnews.util.isEqual
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.ArrayList
import javax.inject.Inject

/**Created by
Author: Ankush Bose
Date: 19,May,2021
 **/

class NewsRepository @Inject constructor(
    private val apiService: ApiService,
    private val dao: NewsDao
) : NewsDataImpl {


    override fun getNewsFromLocal(): Flow<List<NewsEntity?>> = dao.getNews()

    override suspend fun fetchNewsAndSaveToLocal() {
        withContext(Dispatchers.IO) {
            val response: News? = apiService.getNews(SEARCH_KEYWORD)
            val newsArrayList: ArrayList<NewsEntity>? = response?.articles?.convertToNewsEntity()
            // TODO: Check if the news list is changed then only update
            dao.updateNews(newsArrayList)
        }
    }

    companion object {
        const val SEARCH_KEYWORD = "robot"
    }
}