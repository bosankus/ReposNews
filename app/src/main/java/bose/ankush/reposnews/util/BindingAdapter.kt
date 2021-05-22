package bose.ankush.reposnews.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
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
