package bose.ankush.reposnews.util

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import bose.ankush.reposnews.data.local.NewsEntity

fun Fragment.shareNews(newsEntity: NewsEntity) {
    val sharingIntent = Intent(Intent.ACTION_SEND)
    val shareText = newsEntity.content
    val shareSubText = newsEntity.url
    sharingIntent.apply {
        type = "image/*"
        putExtra(Intent.EXTRA_SUBJECT, shareSubText)
        putExtra(Intent.EXTRA_TEXT, shareText)
        putExtra(Intent.EXTRA_STREAM, Uri.parse("${newsEntity.urlToImage}"))
        startActivity(Intent.createChooser(this, "Share news via"))
    }
}