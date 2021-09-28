package bose.ankush.reposnews.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**Created by
Author: Ankush Bose
Date: 20,May,2021
 **/

@Dao
interface NewsDao {

    @Transaction
    suspend fun updateNews(newsList: List<NewsEntity>?) {
        deleteAllNews()
        newsList?.let { list -> list.forEach { insertNews(it) } }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNews(news: NewsEntity)

    @Query("SELECT * FROM news_table")
    fun getNewsViaFlow(): Flow<List<NewsEntity?>>

    @Query("SELECT * FROM news_table")
    fun getNewsLiveData(): LiveData<List<NewsEntity?>>

    @Query("DELETE FROM news_table")
    suspend fun deleteAllNews()

}