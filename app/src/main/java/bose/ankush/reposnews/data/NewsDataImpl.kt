package bose.ankush.reposnews.data

import bose.ankush.reposnews.data.local.NewsEntity

/**Created by
Author: Ankush Bose
Date: 20,May,2021
 **/

interface NewsDataImpl {

    suspend fun getNewsFromLocal(): List<NewsEntity?>
    suspend fun fetchNewsAndSaveToLocal(): List<NewsEntity?>
}