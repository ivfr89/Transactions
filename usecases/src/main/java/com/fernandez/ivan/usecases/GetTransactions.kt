package com.fernandez.ivan.usecases

import com.fernandez.ivan.data.interfaces.ITransactionRepository
import com.fernandez.ivan.domain.Either
import com.fernandez.ivan.domain.Failure
import com.fernandez.ivan.domain.Transaction


class GetTransactions(private val transactionRepository: ITransactionRepository): UseCase<GetTransactions.Params,List<Transaction>>() {


    override suspend fun run(params: Params): Either<Failure, List<Transaction>> =
        transactionRepository.getTransactions(params.forceReload)

    class Params(val forceReload: Boolean=false)
}