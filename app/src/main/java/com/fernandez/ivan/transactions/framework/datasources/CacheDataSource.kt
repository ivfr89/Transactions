package com.fernandez.ivan.transactions.framework.datasources

import com.fernandez.ivan.data.interfaces.ILocalDataSource
import com.fernandez.ivan.domain.Transaction

class CacheDataSource: ILocalDataSource {

    private var cacheTransactions : List<Transaction> = emptyList()

    override suspend fun getTransactions(): List<Transaction> = cacheTransactions

    override fun insertTransactions(list: List<Transaction>) {
        cacheTransactions = list.toList()
    }
}