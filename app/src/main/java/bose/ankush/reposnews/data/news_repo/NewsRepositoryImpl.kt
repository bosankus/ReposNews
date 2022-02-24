package bose.ankush.reposnews.data.news_repo

import bose.ankush.reposnews.data.local.NewsDao
import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.data.local.model.TopHeadlinesIndia
import bose.ankush.reposnews.data.network.NewsApiService
import bose.ankush.reposnews.data.network.toNewsEntityList
import bose.ankush.reposnews.util.bothListsMatch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**Created by
Author: Ankush Bose
Date: 19,May,2021
 **/

class NewsRepositoryImpl @Inject constructor(
    private val newsApiService: NewsApiService,
    private val dao: NewsDao
) : NewsRepository {

    /** Getting the headlines from network via flow */
    override fun getHeadlines(): Flow<TopHeadlinesIndia?> = flow {
        val result: TopHeadlinesIndia? = newsApiService.getTopHeadlinesIndia()
        if (result != null && result.articles.isNotEmpty()) emit(result)
        else emit(null)
    }.flowOn(Dispatchers.IO)


    /** Getting the news from local room db via flow */
    override fun getNewsFromLocal(): Flow<List<NewsEntity?>>? = dao.getNewsViaFlow()


    /** Updating room db with fresh news from network */
    override suspend fun updateNews(): Boolean = withContext(Dispatchers.IO) {
        val isDataMatching: Boolean

        val localData = async { dao.getNewsViaLiveData() }
        val remoteNews = async { newsApiService.getNews(SEARCH_KEYWORD) }

        val old: List<NewsEntity?>? = localData.await()
        val new: List<NewsEntity?>? =
            remoteNews.await()?.articles?.map { article -> article?.toNewsEntityList() }

        isDataMatching =
            ((old?.isNotEmpty() == true && new?.isNotEmpty() == true) && bothListsMatch(old, new))

        if (!isDataMatching) new?.let { dao.updateNews(it) }

        isDataMatching
    }


    /** Bookmarking news item and storing in room db */
    override suspend fun bookmarkNewsItem(newsEntity: NewsEntity) {
        withContext(Dispatchers.IO) {
            if (newsEntity.isBookmarked) dao.bookmarkNewsItem(newsEntity.id, false)
            else dao.bookmarkNewsItem(newsEntity.id, true)
        }
    }

    /** For getting all the bookmarked news items */
    override fun getAllBookmarkedNews(): Flow<List<NewsEntity?>>? = dao.getAllBookmarkedNews()


    companion object {
        const val SEARCH_KEYWORD = "abuse"
    }
}
