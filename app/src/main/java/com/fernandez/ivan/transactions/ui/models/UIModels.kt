package com.fernandez.ivan.transactions.ui.models

import java.text.SimpleDateFormat
import java.util.*

data class UITransaction(
    val id: Int?,
    val date: Long,
    val amount: Float?,
    val fee: Float?,
    val description: String?
){
    fun totalAmount() : Float = amount?.let { amount -> amount + (fee ?: 0f) } ?: 0f
    fun formatDate() : String = SimpleDateFormat(
        "dd .MMM yyyy HH:mm:ss",
        Locale.getDefault()
    ).format(Date(date))

}