package com.fernandez.ivan.transactions.framework

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fernandez.ivan.data.models.ModelEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

inline fun <reified T: ModelEntity> String.fromJsonModelEntity(): T = Gson().fromJson(this, object: TypeToken<T>(){}.type)

fun String.parseInstant(): Long? {
    val utc = TimeZone.getTimeZone("UTC")
    val sourceFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

    sourceFormat.timeZone = utc

    return try {
        sourceFormat.parse(this)?.time

    }catch (e: ParseException){
        null
    }
}

val String.Companion.empty
    get() = ""

fun View.show() {visibility = View.VISIBLE}
fun View.hide() {visibility = View.GONE}


inline fun <VH : RecyclerView.ViewHolder, T> RecyclerView.Adapter<VH>.basicDiffUtil(
    initialValue: List<T>,
    crossinline areItemsTheSame: (T, T) -> Boolean = { old, new -> old == new },
    crossinline areContentsTheSame: (T, T) -> Boolean = { old, new -> old == new }
) =
    Delegates.observable(initialValue) { _, old, new ->
        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                areItemsTheSame(old[oldItemPosition], new[newItemPosition])

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                areContentsTheSame(old[oldItemPosition], new[newItemPosition])

            override fun getOldListSize(): Int = old.size

            override fun getNewListSize(): Int = new.size
        }).dispatchUpdatesTo(this@basicDiffUtil)
    }
