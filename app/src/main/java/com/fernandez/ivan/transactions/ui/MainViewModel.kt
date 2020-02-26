package com.fernandez.ivan.transactions.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fernandez.ivan.domain.Failure
import com.fernandez.ivan.transactions.framework.toUI
import com.fernandez.ivan.usecases.GetTransactions
import kotlinx.coroutines.launch

class MainViewModel(private val transactionUseCase: GetTransactions): ViewModel()
{
    private val _state = MutableLiveData<MainUIState>()
    val state: LiveData<MainUIState>
        get() = _state

    private fun handleTransactionError(failure: Failure)
    {
        _state.postValue(MainUIState.ShowError(failure))
    }

    fun getTransactions(forceReload: Boolean=false)
    {
        _state.value = MainUIState.ShowLoader

        viewModelScope.launch {

            transactionUseCase.execute(GetTransactions.Params(forceReload))
                .fold({handleTransactionError(it)},{list->

                        _state.value = MainUIState.ShowTransactions(
                            list.mapNotNull { it.toUI() }
                                .groupBy { it.id }
                                .map { it.value.maxBy { value-> value.date }!! }
                                .sortedByDescending { it.date }
                        )

                    Unit
                })

        }
    }
}