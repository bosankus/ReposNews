package bose.ankush.reposnews.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import bose.ankush.reposnews.R
import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.view.adapter.NewsAdapter
import com.bumptech.glide.Glide

/**Created by
Author: Ankush Bose
Date: 20,May,2021
 **/

@BindingAdapter("setContentText", "newsLink")
fun TextView.setContentText(txt: String, newsLink: String?) {
    var givenString = "$txt.."
    text = newsLink?.let {
        val link = "<a href=$it>Read More</a></u>"
        HtmlCompat.fromHtml("$givenString $link", HtmlCompat.FROM_HTML_MODE_LEGACY)
    } ?: givenString
}


@BindingAdapter("setNewsImage")
fun ImageView.setImage(url: String?) {
    url?.let {
        Glide.with(this.context)
            .load(it)
            .centerCrop()
            .into(this)
    }
}


@BindingAdapter("hasAnyError")
fun View.errorVisibility(response: ResultData<*>) {
    this.visibility =
        if (response is ResultData.Failed || (response is ResultData.Success && response.data == null))
            View.VISIBLE
        else View.GONE
}


@BindingAdapter("android:text")
fun TextView.errorText(response: ResultData<*>) {
    if (response is ResultData.Failed) text = response.message
    else if (response is ResultData.Success && response.data == null)
        text = resources.getString(R.string.empty_error_txt)
}


@BindingAdapter("isSwipeRefreshing")
fun SwipeRefreshLayout.refreshingVisibility(response: ResultData<*>) {
    this.isRefreshing = response is ResultData.Loading
}

@BindingAdapter("newsList")
fun RecyclerView.setList(response: ResultData<List<NewsEntity?>>) {
    val newsAdapter = NewsAdapter()
    this.adapter = newsAdapter
    if (response is ResultData.Success && response.data != null) newsAdapter.submitList(response.data)
    else newsAdapter.submitList(emptyList())
    this.scheduleLayoutAnimation()
}
