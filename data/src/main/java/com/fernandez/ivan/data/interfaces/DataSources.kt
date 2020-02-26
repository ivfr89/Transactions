package com.fernandez.ivan.data.interfaces

import com.fernandez.ivan.domain.Either
import com.fernandez.ivan.domain.Failure
import com.fernandez.ivan.domain.Transaction

interface IRemoteDataSource
{
    suspend fun getTransactions(): Either<Failure, List<Transaction>>
}

interface ILocalDataSource
{
    suspend fun getTransactions(): List<Transaction>
    fun insertTransactions(list: List<Transaction>)
}