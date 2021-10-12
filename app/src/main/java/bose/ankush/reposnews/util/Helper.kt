package bose.ankush.reposnews.util

import android.view.View
import bose.ankush.reposnews.data.local.NewsEntity
import bose.ankush.reposnews.data.network.News
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

/**Created by
Author: Ankush Bose
Date: 19,May,2021
 **/

fun getCurrentTimestamp(): String = Calendar.getInstance().timeInMillis.toString()

fun logMessage(message: String) = Timber.d(message)

fun showSnack(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
}

fun <T> bothListsMatch(a: List<T?>, b: List<T?>): Boolean {
    return a.size == b.size && a.containsAll(b)
}