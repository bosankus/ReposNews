package bose.ankush.reposnews.util

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**Created by
Author: Ankush Bose
Date: 19,May,2021
 **/

fun getCurrentTimestamp(): String = Calendar.getInstance().timeInMillis.toString()

fun logMessage(message: String) = Timber.d(message)

fun showSnack(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
}

inline fun <reified T> bothListsMatch(first: List<T?>, second: List<T?>): Boolean {
    if (first.size != second.size) {
        return false
    }
    return first.zip(second).all { (x, y) -> x == y }
}

@SuppressLint("SimpleDateFormat")
fun String.showDayDateAndMonth(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val parsedDate = LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
        parsedDate.format(DateTimeFormatter.ofPattern("E, MMM dd yyyy h:mm a"))
    } else {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("E, MMM dd yyyy h:mm a")
        formatter.format(checkNotNull(parser.parse(this)))
    }
}