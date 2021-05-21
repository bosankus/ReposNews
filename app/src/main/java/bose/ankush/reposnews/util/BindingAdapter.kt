package bose.ankush.reposnews.util

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.view.NewsAdapter

/**Created by
Author: Ankush Bose
Date: 20,May,2021
 **/

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
