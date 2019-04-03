package com.listonic.prolificcalendar

import android.content.res.Resources

/**
 * Created in Listonic by mpowloka on 28.08.2018.
 */
fun Int.pixelsToDp() = this / Resources.getSystem().displayMetrics.density

fun Int.dpToPixels() = this * Resources.getSystem().displayMetrics.density

fun Float.pixelsToDp() = this / Resources.getSystem().displayMetrics.density

fun Float.dpToPixels() = this * Resources.getSystem().displayMetrics.density