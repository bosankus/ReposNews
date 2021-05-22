package bose.ankush.reposnews.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**Created by
Author: Ankush Bose
Date: 20,May,2021
 **/

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNews(news: NewsEntity)

    @Query("SELECT * FROM news_table")
    suspend fun getNews(): List<NewsEntity?>

    @Query("SELECT * FROM news_table")
    fun getNewsLiveData(): LiveData<List<NewsEntity?>>

    @Query("DELETE FROM news_table")
    suspend fun deleteAllNews()

}