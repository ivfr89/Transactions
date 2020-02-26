package com.fernandez.ivan.data.interfaces

import com.fernandez.ivan.domain.Either
import com.fernandez.ivan.domain.Failure
import com.fernandez.ivan.domain.Transaction

interface ITransactionRepository
{
    suspend fun getTransactions(forceReload: Boolean): Either<Failure, List<Transaction>>

}