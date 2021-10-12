package bose.ankush.reposnews.data.network


import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.util.getCurrentTimestamp
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class News(
    @Json(name = "status")
    val status: String?,
    @Json(name = "totalResults")
    val totalResults: Int?,
    @Json(name = "articles")
    val articles: List<Article?>?
) {
    @JsonClass(generateAdapter = true)
    data class Article(
        @Json(name = "source")
        val source: Source?,
        @Json(name = "author")
        val author: String?,
        @Json(name = "title")
        val title: String?,
        @Json(name = "description")
        val description: String?,
        @Json(name = "url")
        val url: String?,
        @Json(name = "urlToImage")
        val urlToImage: String?,
        @Json(name = "publishedAt")
        val publishedAt: String?,
        @Json(name = "content")
        val content: String?
    ) {
        @JsonClass(generateAdapter = true)
        data class Source(
            @Json(name = "id")
            val id: String?,
            @Json(name = "name")
            val name: String?
        )
    }
}


fun News.Article.toNewsEntityList(): NewsEntity {
    return NewsEntity(
        content = this.content,
        description = this.description,
        publishedAt = this.publishedAt,
        sourceName = this.source?.name,
        title = this.title,
        url = this.url,
        urlToImage = this.urlToImage,
        newsUpdateTime = getCurrentTimestamp()
    )
}

