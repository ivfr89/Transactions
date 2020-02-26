package com.fernandez.ivan.usecases

import com.fernandez.ivan.data.interfaces.ITransactionRepository
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class GetTransactionsTest {

    @Mock
    lateinit var transactionsRepository: ITransactionRepository

    private lateinit var getTransactionsUseCase: GetTransactions

    @Before
    fun setUp() {
        getTransactionsUseCase = GetTransactions(transactionsRepository)
    }

    @Test
    fun `getTransactions invokes ITransactionRepository`()
    {
        runBlocking {
            val forceReload = GetTransactions.Params(anyBoolean())

            getTransactionsUseCase.execute(forceReload)

            verify(transactionsRepository).getTransactions(forceReload.forceReload)
        }

    }
}