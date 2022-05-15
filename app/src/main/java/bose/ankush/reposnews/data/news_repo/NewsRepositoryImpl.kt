package bose.ankush.reposnews.data.news_repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import bose.ankush.reposnews.data.local.NewsDatabase
import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.data.model.TopHeadlinesIndia
import bose.ankush.reposnews.data.network.NewsApiService
import bose.ankush.reposnews.data.network.NewsMediator
import bose.ankush.reposnews.util.ITEMS_PER_PAGE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**Created by
Author: Ankush Bose
Date: 19,May,2021
 **/

@ExperimentalPagingApi
class NewsRepositoryImpl @Inject constructor(
    private val newsApiService: NewsApiService,
    private val db: NewsDatabase
) : NewsRepository {

    /** Getting the headlines from network via flow */
    override fun getHeadlines(): Flow<TopHeadlinesIndia?> = flow {
        val result: TopHeadlinesIndia? = newsApiService.getTopHeadlinesIndia()
        if (result != null && result.articles.isNotEmpty()) emit(result)
        else emit(null)
    }.flowOn(Dispatchers.IO)


    /** Getting the paginated news data from local room db via flow */
    override fun getNewsFromLocal(): Flow<PagingData<NewsEntity>> {
        val pagingSourceFactory = { db.newsDao().getPagingNews() }
        return Pager(
            config = PagingConfig(
                pageSize = ITEMS_PER_PAGE,
                enablePlaceholders = false
            ),
            remoteMediator = NewsMediator(newsApiService, db),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }


    /** Bookmarking news item and storing in room db */
    override suspend fun bookmarkNewsItem(newsEntity: NewsEntity) {
        withContext(Dispatchers.IO) {
            if (newsEntity.isBookmarked) db.newsDao().bookmarkNewsItem(newsEntity.id, false)
            else db.newsDao().bookmarkNewsItem(newsEntity.id, true)
        }
    }
}
