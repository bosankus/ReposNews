package bose.ankush.reposnews.data.news_repo

import androidx.paging.PagingData
import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.data.model.TopHeadlinesIndia
import kotlinx.coroutines.flow.Flow

/**Created by
Author: Ankush Bose
Date: 20,May,2021
 **/

interface NewsRepository {

    fun getHeadlines(): Flow<TopHeadlinesIndia?>

    fun getNewsFromLocal(): Flow<PagingData<NewsEntity>>

    /*suspend fun updateNews(): Boolean*/

    fun getNewsFromSearchKeyword(query: String): Flow<PagingData<NewsEntity>>

    suspend fun bookmarkNewsItem(newsEntity: NewsEntity)

    fun getAllBookmarkedNews(): Flow<List<NewsEntity?>>?
}