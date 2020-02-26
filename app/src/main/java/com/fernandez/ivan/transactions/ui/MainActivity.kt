package com.fernandez.ivan.transactions.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.fernandez.ivan.domain.Failure
import com.fernandez.ivan.transactions.R
import com.fernandez.ivan.transactions.framework.hide
import com.fernandez.ivan.transactions.framework.show
import com.fernandez.ivan.transactions.ui.adapter.TransactionAdapter
import com.fernandez.ivan.transactions.ui.models.UITransaction
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {


    private val mViewModel: MainViewModel by inject()
    private lateinit var mAdapter: TransactionAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initStates()
        initListeners()
        configureUI()

        mViewModel.getTransactions(savedInstanceState==null)
    }

    private fun initStates()
    {
        mViewModel.state.observe(this, Observer {
            renderScreenState(it)
        })
    }

    private fun configureUI()
    {
        mAdapter = TransactionAdapter()

        rcvTransactions.adapter = mAdapter
    }

    private fun initListeners()
    {
        swipeRefresh.setOnRefreshListener {
            mViewModel.getTransactions(true)
        }
    }

    private fun showLoader()
    {
        swipeRefresh.isRefreshing = true
    }

    private fun hideLoader()
    {
        swipeRefresh.isRefreshing = false

    }

    private fun renderScreenState(uiScreenState: MainUIState)
    {
        when(uiScreenState)
        {
            is MainUIState.ShowLoader -> showLoader()
            is MainUIState.ShowError -> handleUIError(uiScreenState.failure)
            is MainUIState.ShowTransactions -> handleTransactions(uiScreenState.list)
        }
    }

    private fun handleTransactions(transactions: List<UITransaction>) {

        hideLoader()
        mAdapter.items = transactions

        if(transactions.isNotEmpty())
            containerEmpty.hide()
        else{
            containerEmpty.show()

        }

    }

    private fun handleUIError(failure: Failure)
    {
        hideLoader()
        when(failure)
        {
            Failure.NetworkConnection -> {
                Toast.makeText(this,getString(R.string.no_connectivity),Toast.LENGTH_SHORT).show()
                containerEmpty.show()
                mAdapter.items = listOf()
            }
            is Failure.ServerErrorCode -> Toast.makeText(this,getString(R.string.server_error_code,failure.code.toString()),Toast.LENGTH_SHORT)
            is Failure.ServerException -> Toast.makeText(this,failure.throwable.localizedMessage,Toast.LENGTH_SHORT).show()
            is Failure.JsonException -> Toast.makeText(this,getString(R.string.json_exception),Toast.LENGTH_SHORT).show()
        }
    }


}