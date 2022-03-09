package bose.ankush.reposnews.data.model

data class TopHeadlinesIndia(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)