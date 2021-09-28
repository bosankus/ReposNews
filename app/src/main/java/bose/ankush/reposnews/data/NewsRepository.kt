package bose.ankush.reposnews.data

import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.data.network.News
import kotlinx.coroutines.flow.Flow

/**Created by
Author: Ankush Bose
Date: 20,May,2021
 **/

interface NewsRepository {

    fun getNewsFromLocal(): Flow<List<NewsEntity?>>

    suspend fun getNewsFromRemote(): News?

    suspend fun updateLocalWithUpdatedNews(news: List<NewsEntity>)
}