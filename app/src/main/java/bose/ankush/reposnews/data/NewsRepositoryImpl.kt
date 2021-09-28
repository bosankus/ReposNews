package bose.ankush.reposnews.data

import bose.ankush.reposnews.data.local.NewsDao
import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.data.network.ApiService
import bose.ankush.reposnews.data.network.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**Created by
Author: Ankush Bose
Date: 19,May,2021
 **/

class NewsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dao: NewsDao
) : NewsRepository {


    override fun getNewsFromLocal(): Flow<List<NewsEntity?>> = dao.getNewsViaFlow()

    override suspend fun getNewsFromRemote(): News? =
        withContext(Dispatchers.IO) { apiService.getNews(SEARCH_KEYWORD) }

    override suspend fun updateLocalWithUpdatedNews(news: List<NewsEntity>) =
        withContext(Dispatchers.IO) { dao.updateNews(news) }

    companion object {
        const val SEARCH_KEYWORD = "vaccine"
    }
}