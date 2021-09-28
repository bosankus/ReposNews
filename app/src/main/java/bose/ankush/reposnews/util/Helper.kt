package bose.ankush.reposnews.util

import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.data.network.News

/**Created by
Author: Ankush Bose
Date: 19,May,2021
 **/


/*fun logMessage(message: String) = Log.d("Ankush", message)

fun showSnack(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
}*/

fun <T> bothListsMatch(a: List<T>, b: List<T>): Boolean {
    return a.size == b.size && a.containsAll(b)
}

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