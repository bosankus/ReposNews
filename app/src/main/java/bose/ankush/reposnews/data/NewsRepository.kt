package bose.ankush.reposnews.data

import bose.ankush.reposnews.data.local.NewsEntity
import kotlinx.coroutines.flow.Flow

/**Created by
Author: Ankush Bose
Date: 20,May,2021
 **/

interface NewsRepository {

    fun getNewsFromLocal(): Flow<List<NewsEntity?>>?

    suspend fun updateNews(): Boolean
}