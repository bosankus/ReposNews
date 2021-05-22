package bose.ankush.reposnews.util

import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar

/**Created by
Author: Ankush Bose
Date: 19,May,2021
 **/


fun logMessage(message: String) = Log.d("Ankush", message)

fun showSnack(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
}