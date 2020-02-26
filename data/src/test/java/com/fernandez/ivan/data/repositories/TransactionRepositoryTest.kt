package com.fernandez.ivan.data.repositories

import com.fernandez.ivan.data.interfaces.ILocalDataSource
import com.fernandez.ivan.data.interfaces.INetwork
import com.fernandez.ivan.data.interfaces.IRemoteDataSource
import com.fernandez.ivan.data.interfaces.ITransactionRepository
import com.fernandez.ivan.domain.Either
import com.fernandez.ivan.domain.Failure
import com.fernandez.ivan.testshared.mockedTransactions
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class TransactionRepositoryTest {


    @Mock
    lateinit var localDataSource: ILocalDataSource

    @Mock
    lateinit var remoteDataSource: IRemoteDataSource

    @Mock
    lateinit var networkManager: INetwork

    private lateinit var transactionRepository: ITransactionRepository

    @Before
    fun setUp() {
        transactionRepository = TransactionRepository(remoteDataSource, localDataSource, networkManager)
    }


    @Test
    fun `getTransactions always call the LocalDataSource`(){

        runBlocking {

            whenever(localDataSource.getTransactions()).thenReturn(mockedTransactions)

            transactionRepository.getTransactions(false)


            verify(localDataSource).getTransactions()

        }
    }

    @Test
    fun `getTransactions (force) saves remote data to local`(){
        runBlocking {

            whenever(localDataSource.getTransactions()).thenReturn(listOf())
            whenever(networkManager.isConnected).thenReturn(true)
            whenever(remoteDataSource.getTransactions()).thenReturn(Either.Right(mockedTransactions))

            transactionRepository.getTransactions(true)

            verify(localDataSource).insertTransactions(mockedTransactions)
        }
    }

    @Test
    fun `getTransactions local empty without connection returns FailureNetworkConnection`(){
        runBlocking {

            whenever(localDataSource.getTransactions()).thenReturn(listOf())

            whenever(networkManager.isConnected).thenReturn(false)

            val result = transactionRepository.getTransactions(true)

            assertEquals(result,Either.Left(Failure.NetworkConnection))
        }
    }


}