package bose.ankush.reposnews.data

import bose.ankush.reposnews.data.local.NewsDao
import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.data.network.ApiService
import bose.ankush.reposnews.util.convertToNewsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**Created by
Author: Ankush Bose
Date: 19,May,2021
 **/

class NewsRepository @Inject constructor(
    private val apiService: ApiService,
    private val dao: NewsDao
) : NewsDataImpl {

    override suspend fun getNewsFromLocal(): List<NewsEntity?> = dao.getNews()

    override suspend fun fetchNewsAndSaveToLocal(): List<NewsEntity?> {
        dao.deleteAllNews()
        withContext(Dispatchers.IO) {
            val response = apiService.getNews(SEARCH_KEYWORD)
            response?.articles?.convertToNewsEntity()?.forEach { dao.insertNews(it) }
        }
        return dao.getNews()
    }

    companion object {
        const val SEARCH_KEYWORD = "energy"
    }
}