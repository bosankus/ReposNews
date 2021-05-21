package bose.ankush.reposnews.data

import androidx.lifecycle.LiveData
import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.data.network.News

/**Created by
Author: Ankush Bose
Date: 20,May,2021
 **/

interface NewsDataImpl {

    suspend fun getNewsFromLocal(): List<NewsEntity?>
    suspend fun fetchNewsAndSaveToLocal(): List<NewsEntity?>
}