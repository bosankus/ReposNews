package bose.ankush.reposnews.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "remote_keys")
@Parcelize
data class NewsRemoteKeysEntity(
    @PrimaryKey val newsId: Int?,
    val prevKey: Int?,
    val nextKey: Int?
) : Parcelable
