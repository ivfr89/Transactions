package com.fernandez.ivan.transactions.framework

import com.fernandez.ivan.domain.Transaction
import com.fernandez.ivan.transactions.ui.models.UITransaction

fun Transaction.toUI(): UITransaction?{

    val date = date?.parseInstant()
    return if(date!=null)
    {
        UITransaction(id,
            date,
            amount,
            fee,
            description?.trim()
        )
    }else
        null
}
