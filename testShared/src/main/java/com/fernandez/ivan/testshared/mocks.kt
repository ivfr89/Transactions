package com.fernandez.ivan.testshared

import com.fernandez.ivan.domain.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description

val mockedTransactions = listOf(
    Transaction(1,"2018-07-11T22:49:24.000Z",-10f,2f,"first mock "),
    Transaction(2,"07-22T13:51:12.000Z",10f,2f,"second mock "),
    Transaction(3,null,1f,0f,"third mock ")
)

/*
@ExperimentalCoroutinesApi
class CoroutinesTestRule(
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    fun runBlockingTest(testCoroutineScope : suspend TestCoroutineScope.()->Unit) =
        testDispatcher.runBlockingTest { testCoroutineScope(this) }
}*/
