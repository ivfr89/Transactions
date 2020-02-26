package com.fernandez.ivan.data.repositories

import com.fernandez.ivan.data.interfaces.ILocalDataSource
import com.fernandez.ivan.data.interfaces.INetwork
import com.fernandez.ivan.data.interfaces.ITransactionRepository
import com.fernandez.ivan.data.interfaces.IRemoteDataSource
import com.fernandez.ivan.domain.*


class TransactionRepository(private val remoteDataSource: IRemoteDataSource,
                            private val localDataSource: ILocalDataSource,
                            private val networkManager: INetwork) : ITransactionRepository
{
    override suspend fun getTransactions(forceReload: Boolean): Either<Failure, List<Transaction>>
    {


        val data = localDataSource.getTransactions()

        return when{
            data.isNotEmpty() && !forceReload -> Either.Right(localDataSource.getTransactions())
            else -> {
                if(networkManager.isConnected){

                    remoteDataSource.getTransactions().map {
                        localDataSource.insertTransactions(it)
                        it
                    }
                }else{
                    Either.Left(Failure.NetworkConnection)
                }
            }
        }

    }

}