package bose.ankush.reposnews.data

import bose.ankush.reposnews.data.local.NewsDao
import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.data.network.ApiService
import bose.ankush.reposnews.data.network.toNewsEntityList
import bose.ankush.reposnews.util.bothListsMatch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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


    override fun getNewsFromLocal(): Flow<List<NewsEntity?>>? = dao.getNewsViaFlow()

    override suspend fun updateNews(): Boolean = withContext(Dispatchers.IO) {
        val isDataMatching: Boolean

        val localData = async { dao.getNewsViaLiveData() }
        val remoteNews = async { apiService.getNews(SEARCH_KEYWORD) }

        val old: List<NewsEntity?>? = localData.await()
        val new: List<NewsEntity?>? =
            remoteNews.await()?.articles?.map { article -> article?.toNewsEntityList() }

        isDataMatching = ((old != null && new != null) && bothListsMatch(old, new))

        if (!isDataMatching) new?.let { dao.updateNews(it) }

        isDataMatching
    }

    companion object {
        const val SEARCH_KEYWORD = "BMW"
    }
}
