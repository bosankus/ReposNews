package bose.ankush.reposnews.data.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import bose.ankush.reposnews.data.local.NewsDatabase
import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.data.local.NewsRemoteKeysEntity
import bose.ankush.reposnews.data.model.News
import bose.ankush.reposnews.data.model.toNewsEntityList
import bose.ankush.reposnews.util.SEARCH_KEYWORD
import bose.ankush.reposnews.util.START_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class NewsMediator(
    private val newsApiService: NewsApiService,
    private val newsDatabase: NewsDatabase
) : RemoteMediator<Int, NewsEntity>() {

    override suspend fun initialize(): InitializeAction {
        // Launching paging with refresh first, without triggering prepend or append.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NewsEntity>
    ): MediatorResult {
        // calculating page number as per load type.
        val page = when (loadType) {
            LoadType.APPEND -> {
                val remoteKey = newsDatabase.newsRemoteKeysDao().getRemoteKeys().lastOrNull()
                val nextKey = remoteKey?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
                nextKey
            }
            LoadType.PREPEND -> {
                val remoteKey = newsDatabase.newsRemoteKeysDao().getRemoteKeys().firstOrNull()
                val prevKey = remoteKey?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                prevKey
            }
            LoadType.REFRESH -> {
                val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
                remoteKey?.nextKey?.minus(1) ?: START_PAGE_INDEX
            }
        }

        try {
            // making network call
            val news: News? = newsApiService.getNews(page, state.config.pageSize, SEARCH_KEYWORD)

            // converting response to NewsEntity and storing list of articles only
            val listOfArticles: List<NewsEntity?> =
                news?.articles?.map { it?.toNewsEntityList() } ?: emptyList()

            // storing if user list is empty
            val endOfPaginationReached = listOfArticles.isEmpty()

            newsDatabase.withTransaction {
                // clear all table in database
                if (loadType == LoadType.REFRESH) {
                    newsDatabase.newsRemoteKeysDao().clearRemoteKeys()
                    newsDatabase.newsDao().deleteAllNews()
                }

                // storing previous and next keys
                val prevKey = if (page == START_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                // storing the remote key and fetched article
                val keys: List<NewsRemoteKeysEntity?> = listOfArticles.map { newsEntity ->
                    newsEntity?.let {
                        NewsRemoteKeysEntity(newsId = it.id, prevKey = prevKey, nextKey = nextKey)
                    }
                }
                newsDatabase.newsRemoteKeysDao().insertAll(keys)
                newsDatabase.newsDao().insertNews(listOfArticles)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }


    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, NewsEntity>): NewsRemoteKeysEntity? {
        // Getting the remote key closest to anchor position
        // Paging library will load data after the anchor position
        // and get that item, which is closest to anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { userId ->
                newsDatabase.newsRemoteKeysDao().remoteKeysRepoId(userId)
            }
        }
    }
}