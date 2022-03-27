package bose.ankush.reposnews.data.local

import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**Created by
Author: Ankush Bose
Date: 20,May,2021
 **/

@Dao
interface NewsDao {

    @Transaction
    suspend fun updateNews(newsList: List<NewsEntity>) {
        deleteUnBookmarkedNews()
        insertNews(newsList)
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNews(news: List<NewsEntity?>)

    @Query("SELECT * FROM news_table")
    fun getPagingNews(): PagingSource<Int, NewsEntity>

    @Query("SELECT * FROM news_table WHERE isBookmarked = 1 ORDER BY publishedAt DESC")
    fun getAllBookmarkedNews(): Flow<List<NewsEntity?>>?

    @Query("SELECT * FROM news_table WHERE title LIKE '%' || :keyword || '%'")
    fun getNewsFromSearchKeyword(keyword: String): PagingSource<Int, NewsEntity>

    @Query("UPDATE news_table SET isBookmarked = :isBookmarked WHERE id= :newsId")
    fun bookmarkNewsItem(newsId: Int, isBookmarked: Boolean)

    @Query("DELETE FROM news_table WHERE isBookmarked = 0")
    suspend fun deleteUnBookmarkedNews()

    @Query("DELETE from news_table")
    suspend fun deleteAllNews()

}