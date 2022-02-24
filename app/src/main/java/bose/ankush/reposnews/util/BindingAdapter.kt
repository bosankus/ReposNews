package bose.ankush.reposnews.util

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import bose.ankush.reposnews.R
import bose.ankush.reposnews.data.local.model.Weather
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

/**Created by
Author: Ankush Bose
Date: 20,May,2021
 **/

/*TextView bindings*/

@BindingAdapter("android:text")
fun TextView.errorText(response: ResultData<*>) {
    if (response is ResultData.Failed) text = response.message
    else if (response is ResultData.Success && response.data == null)
        text = resources.getString(R.string.empty_error_txt)
}

@BindingAdapter("setContentText", "newsLink")
fun TextView.setContentText(txt: String, newsLink: String?) {
    val givenString = "$txt.."
    text = newsLink?.let {
        val link = "<a href=$it>Read More</a></u>"
        HtmlCompat.fromHtml("$givenString $link", HtmlCompat.FROM_HTML_MODE_LEGACY)
    } ?: givenString
}

@SuppressLint("SetTextI18n")
@BindingAdapter("sourceName", "author")
fun TextView.setSourceText(sourceName: String?, author: String?) {
    this.text = if (sourceName != null && author != null) "$sourceName | $author"
    else "Source details not available"
}

@BindingAdapter("setNewsTime")
fun TextView.setNewsTimeText(txt: String?) {
    txt?.let {
        // if OS is above Android O then change format, else show as it is.
        this.text =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) "${it.showDayDateAndMonth()} | "
            else "$it | "
    }
}

@BindingAdapter("setTempInCelsius")
fun TextView.setTempInCelsius(value: ResultData<*>) {
    text =
        if (value is ResultData.Success<*> && value.data is Weather)
            "${value.data.main?.temp?.toCelsius()}°C"
        else "-°C"
}

@BindingAdapter("setCurrentCity")
fun TextView.setCurrentCity(value: ResultData<*>) {
    text =
        if (value is ResultData.Success<*> && value.data is Weather && value.data.name != null)
            value.data.name
        else "..."
}


/* ImageView bindings*/

@BindingAdapter("setNewsImage")
fun ImageView.setImage(url: String?) {
    url?.let {
        Glide.with(this.context)
            .load(it)
            .transform(CenterCrop(), RoundedCorners(30))
            .into(this)
    }
}

@BindingAdapter("setBookmarkIndicator")
fun ImageView.setIndicator(isBookmarked: Boolean) {
    if (isBookmarked) Glide.with(this.context)
        .load(R.drawable.ic_selected_bookmark)
        .into(this)
    else Glide.with(this.context)
        .load(R.drawable.ic_unselected_bookmark)
        .into(this)
}


/* General Layout bindings*/

@BindingAdapter("hasAnyError")
fun View.errorVisibility(response: ResultData<*>) {
    this.visibility =
        if (response is ResultData.Failed || (response is ResultData.Success && response.data == null))
            View.VISIBLE
        else View.GONE
}

@BindingAdapter("isHeadlinesLoading", "isNewsLoading")
fun View.loadingVisibility(headlinesState: ResultData<*>, newsState: ResultData<*>) {
    visibility =
        if (headlinesState is ResultData.Loading || newsState is ResultData.Loading) View.VISIBLE
        else View.GONE
}

@BindingAdapter("isDataLoading")
fun View.layoutVisibleWhenNotLoading(dataState: ResultData<*>?) {
    dataState?.let {
        visibility =
            if (dataState !is ResultData.Loading) View.VISIBLE
            else View.GONE
    }
}


/*SwipeRefreshLayout bindings*/

@BindingAdapter("getNewsState")
fun SwipeRefreshLayout.getNewsState(response: ResultData<*>) {
    this.isRefreshing = response is ResultData.Loading
}

@BindingAdapter("newsUpdateListener")
fun SwipeRefreshLayout.newsUpdateListener(isFreshNewsAvailable: Boolean) {
    if (!isFreshNewsAvailable && isRefreshing) showSnack(this.rootView, "News updated")
    else if (isFreshNewsAvailable && isRefreshing) showSnack(this.rootView, "Already updated")
    isRefreshing = false
}
