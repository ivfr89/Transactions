package com.fernandez.ivan.transactions.ui

import com.fernandez.ivan.domain.Failure
import com.fernandez.ivan.transactions.ui.models.UITransaction

sealed class MainUIState
{
    object ShowLoader: MainUIState()
    class ShowError(val failure: Failure): MainUIState()
    class ShowTransactions(val list: List<UITransaction>): MainUIState()

}