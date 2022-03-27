package bose.ankush.reposnews.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**Created by
Author: Ankush Bose
Date: 20,May,2021
 **/

@Database(
    entities = [NewsEntity::class, NewsRemoteKeysEntity::class],
    version = 3,
    exportSchema = false
)

abstract class NewsDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao

    abstract fun newsRemoteKeysDao(): NewsRemoteKeysDao
}