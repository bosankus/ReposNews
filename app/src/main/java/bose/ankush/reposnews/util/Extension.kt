package bose.ankush.reposnews.util

import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.data.network.News

/**Created by
Author: Ankush Bose
Date: 20,May,2021
 **/

fun List<News.Article?>.convertToNewsEntity(): ArrayList<NewsEntity> {
    val newsEntityList = ArrayList<NewsEntity>()
    newsEntityList += this.map { article ->
        NewsEntity(
            article?.author,
            article?.content,
            article?.description,
            article?.publishedAt,
            article?.source?.name,
            article?.title,
            article?.url,
            article?.urlToImage
        )
    }
    return newsEntityList
}


fun <T> isEqual(first: List<T>, second: List<T>): Boolean {
    return first.size == second.size && first.containsAll(second)
}