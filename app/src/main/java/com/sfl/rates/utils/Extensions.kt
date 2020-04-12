package com.sfl.rates.utils

import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.os.CountDownTimer
import android.os.Handler
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

typealias Callback = () -> Unit

fun Context?.alertDialogWithCustomView(@LayoutRes layoutResId: Int): View? {
    this?.apply {
        val builder = AlertDialog.Builder(this)
        val mView = LayoutInflater.from(this).inflate(layoutResId, null)
        val dialog = builder.create()
        dialog.show()
        return mView
    }
    return null
}


fun safeCall(run: Callback, onFail: Callback) {
    return try {
        run()
    } catch (ignore: Throwable) {
        onFail()
    }
}

fun safeCall(run: Callback) {
    try {
        run()
    } catch (ignore: Exception) {

    }
}


fun String.removeSpaces(): String {
    val result = this
    return result.replace(" ", "")
}

fun String.toFormattedDate(fromFormat: String, toFormat: String): String {
    var result = this
    val fromFormatSdf = SimpleDateFormat(fromFormat, Locale.getDefault())
    val toFormatSdf = SimpleDateFormat(toFormat, Locale.getDefault())
    safeCall {
        val date = fromFormatSdf.parse(result)
        result = toFormatSdf.format(date).orEmpty()
    }
    return result
}

/**
 * Same as toFormattedDate(), first parameter is default yyyy-MM-dd'T'HH:mm:ss.S'Z' format
 * */
fun String.toFormattedDate(toFormat: String): String {
    return toFormattedDate("yyyy-MM-dd'T'HH:mm:ss.S'Z'", toFormat)
}

fun <T> ArrayList<T>?.orEmpty(): ArrayList<T> = this ?: ArrayList()
private var checkerForExecution = true
private val timer = SimpleTimer<Boolean>(20000) {
    checkerForExecution = true
}

fun executeSingleTime(a: Callback) {
    if (checkerForExecution) {
        a.invoke()
        checkerForExecution = false
    }
    timer.restart(null)
}

fun Int.toHourAndMinute(): Pair<Int, Int> {
    val hours = this / 60
    val minutes = this % 60
    return Pair(hours, minutes)
}

fun Long.toFormattedDate(toFormat: String): String {
    var result = ""
    val toFormatSdf = SimpleDateFormat(toFormat, Locale.getDefault())
    safeCall {
        result = toFormatSdf.format(this)
    }
    return result
}

fun Double?.orZero() = this ?: 0.0


fun runDelayed(delay: Long, delayedFunction: () -> Unit) {
    Handler().postDelayed({
        delayedFunction.invoke()
    }, delay)
}

fun String.reverseCombine(): String {
    val x = this.split(",").reversed()
    var returnValue = ""
    x.forEachIndexed { index, s ->
        if (index != x.size && index != 0) {
            returnValue += ", "
        }
        returnValue += s
    }
    return returnValue
}

fun <R> RecyclerView.initRecyclerView(
    context: Context?,
    adapter: R,
    isVertical: Boolean = true,
    useItemDecorator: Boolean = false
) {
    context?.let {
        this.layoutManager = LinearLayoutManager(
            context,
            if (isVertical) LinearLayoutManager.VERTICAL else LinearLayoutManager.HORIZONTAL,
            false
        )

        this.adapter = adapter as RecyclerView.Adapter<*>
        if (useItemDecorator) {
            val dividerItemDecoration = DividerItemDecoration(
                this.context,
                (this.layoutManager as LinearLayoutManager).orientation
            )
            this.addItemDecoration(dividerItemDecoration)
        }
    }
}

fun RecyclerView.initPositionChangeListener(needToSaveCurrentState: (Int) -> Unit) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                val firstVisibleItem =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                needToSaveCurrentState?.invoke(firstVisibleItem)
            }
        }

    })
}

fun Context?.dpToPx(dp: Float): Int {
    this?.let {
        val density = it.resources.displayMetrics.density
        return (dp * density).roundToInt()
    }
    return -1
}

class SimpleTimer<T>(millis: Long, val func: (T?) -> Unit) : CountDownTimer(millis, millis) {
    private var param: T? = null
    override fun onFinish() = func(param)

    override fun onTick(millisUntilFinished: Long) {}

    fun restart(withParam: T? = null) {
        param = withParam
        cancel()
        start()
    }

}

fun EditText.clearText() {
    this.setText("")
}


fun Activity.getDisplayHeightWidth(): Pair<Int, Int> {
    val displayMetrics = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(displayMetrics)
    return Pair(displayMetrics.heightPixels, displayMetrics.widthPixels)
}

fun AppCompatImageView.loadImage(imageUrl: String?, hardDiskCache: Boolean = true) {
    imageUrl?.let {
        Glide.with(this.context).load(imageUrl)
            .diskCacheStrategy(if (hardDiskCache) DiskCacheStrategy.ALL else DiskCacheStrategy.NONE)
            .into(this)
    }
}


fun String.convertToCorrespondingDateFormat(): String {
    var parse: Date? = null
    try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        parse = dateFormat.parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    val dateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm")
    return dateFormat.format(parse)

}


fun Context.getColorFromResources(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}

fun Context.getStringFromResources(@StringRes id: Int): String {
    return this.resources.getString(id)
}


fun <T> LiveData<T>.observeOnce(observer: androidx.lifecycle.Observer<T>) {
    observeForever(object : androidx.lifecycle.Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}


fun TextView.underline() {
    this.paintFlags = Paint.UNDERLINE_TEXT_FLAG
}


inline fun <T> T?.letUnLet(notNull: (T) -> Unit, whenNull: () -> Unit) {
    if (this == null) {
        whenNull.invoke()
    } else {
        notNull.invoke(this)
    }
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
fun Context.toast(@StringRes message: Int) =
    Toast.makeText(this, this.resources.getString(message), Toast.LENGTH_SHORT).show()

fun View.isHidden() = this.visibility == View.GONE || this.visibility == View.INVISIBLE
fun View.isVisible() = this.visibility == View.VISIBLE
fun View.isInvisible() = this.visibility == View.INVISIBLE
fun View.showIf(condition: Boolean) = if (condition) show() else hide()
fun View.hideIf(condition: Boolean) = if (condition) hide() else show()

