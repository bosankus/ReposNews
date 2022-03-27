package bose.ankush.reposnews.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**Created by
Author: Ankush Bose
Date: 20,May,2021
 **/

@Entity(tableName = "news_table")
@Parcelize
data class NewsEntity(
    @ColumnInfo(name = "content")
    val content: String? = "",
    @ColumnInfo(name = "description")
    val description: String? = "",
    @ColumnInfo(name = "author")
    val author: String? = "",
    @ColumnInfo(name = "publishedAt")
    val publishedAt: String? = "",
    @ColumnInfo(name = "sourceName")
    val sourceName: String? = "",
    @ColumnInfo(name = "title")
    val title: String? = "",
    @ColumnInfo(name = "url")
    val url: String? = "",
    @ColumnInfo(name = "urlToImage")
    val urlToImage: String? = "",
    @ColumnInfo(name = "newsUpdateTime")
    val newsUpdateTime: String = "",
    @ColumnInfo(name = "isBookmarked")
    val isBookmarked: Boolean = false
) : Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
