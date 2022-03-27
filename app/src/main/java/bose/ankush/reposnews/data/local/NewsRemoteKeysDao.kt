package bose.ankush.reposnews.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<NewsRemoteKeysEntity?>)

    @Query("SELECT * FROM remote_keys")
    suspend fun getRemoteKeys(): List<NewsRemoteKeysEntity>

    @Query("SELECT * FROM remote_keys WHERE newsId = :newsId")
    suspend fun remoteKeysRepoId(newsId: Int): NewsRemoteKeysEntity?

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}
