package bose.ankush.reposnews.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**Created by
Author: Ankush Bose
Date: 20,May,2021
 **/

@Dao
interface NewsDao {

    @Transaction
    suspend fun updateNews(newsList: List<NewsEntity?>?) {
        deleteUnBookmarkedNews()
        newsList?.let { list -> list.forEach { item -> item?.let { insertNews(it) } } }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNews(news: NewsEntity)

    @Query("DELETE FROM news_table WHERE isBookmarked = 0")
    suspend fun deleteUnBookmarkedNews()

    @Query("SELECT * FROM news_table")
    fun getNewsViaFlow(): Flow<List<NewsEntity?>>?

    @Query("SELECT * FROM news_table")
    fun getNewsViaLiveData(): List<NewsEntity?>?

    @Query("UPDATE news_table SET isBookmarked = :isBookmarked WHERE id= :newsId")
    fun bookmarkNewsItem(newsId: Int, isBookmarked: Boolean)

    @Query("SELECT * FROM news_table WHERE isBookmarked = 1 ORDER BY publishedAt DESC")
    fun getAllBookmarkedNews(): Flow<List<NewsEntity?>>?

}