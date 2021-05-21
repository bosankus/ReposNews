package bose.ankush.reposnews.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.view.NewsAdapter
import com.bumptech.glide.Glide

/**Created by
Author: Ankush Bose
Date: 20,May,2021
 **/

@BindingAdapter("setDescriptionText")
fun TextView.setDescriptionText(txt: String) {
    var givenString = txt
    val lengthOfString = givenString.length
    text =
        if (lengthOfString > 200) {
            val newStringSubset = givenString.substring(0, 200)
            val remainingLengthOfString = lengthOfString - newStringSubset.length
            givenString = "$newStringSubset.. [+$remainingLengthOfString chars]";
            HtmlCompat.fromHtml(
                "$givenString<font color='white'> <u>Read More</u></font>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        } else HtmlCompat.fromHtml(
            "$givenString<font color='white'> <u>Read More</u></font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
}


@BindingAdapter("setNewsImage")
fun ImageView.setImage(url: String) {
    Glide.with(this.context)
        .load(url)
        .centerCrop()
        .into(this)
}


@BindingAdapter("hasAnyError")
fun View.errorVisibility(response: ResultData<*>) {
    this.visibility =
        if (response is ResultData.Failed) View.VISIBLE
        else View.GONE
}


@BindingAdapter("isLoading")
fun View.loadingVisibility(response: ResultData<*>) {
    this.visibility =
        if (response is ResultData.Loading) View.VISIBLE
        else View.GONE
}


@BindingAdapter("newsList")
fun RecyclerView.setList(response: ResultData<List<NewsEntity?>>) {
    val newsAdapter = NewsAdapter()
    this.adapter = newsAdapter
    if (response is ResultData.Success && response.data != null) {
        newsAdapter.submitList(response.data)
    } else newsAdapter.submitList(emptyList())
}
