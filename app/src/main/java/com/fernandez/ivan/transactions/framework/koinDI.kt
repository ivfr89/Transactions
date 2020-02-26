package com.fernandez.ivan.transactions.framework

import com.fernandez.ivan.data.interfaces.ILocalDataSource
import com.fernandez.ivan.data.interfaces.INetwork
import com.fernandez.ivan.data.interfaces.IRemoteDataSource
import com.fernandez.ivan.data.interfaces.ITransactionRepository
import com.fernandez.ivan.data.repositories.TransactionRepository
import com.fernandez.ivan.transactions.framework.datasources.CacheDataSource
import com.fernandez.ivan.transactions.framework.datasources.RetrofitDataSource
import com.fernandez.ivan.transactions.framework.server.ApiService
import com.fernandez.ivan.transactions.framework.server.NetworkHandler
import com.fernandez.ivan.transactions.framework.server.ServerMapper
import com.fernandez.ivan.transactions.ui.MainViewModel
import com.fernandez.ivan.usecases.GetTransactions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit



val networkModule  = module {
    single {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS).build()
    }

    //API Service
    single {
        Retrofit.Builder()
            .baseUrl(Constants.END_POINT_URL)
            .client(get())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build().create(ApiService::class.java)
    }

    single<INetwork> {
        NetworkHandler(androidApplication())
    }

    factory { ServerMapper() }

}

val dataModule = module {
    single<ILocalDataSource> {
        CacheDataSource()
    }

    factory<IRemoteDataSource> {
        RetrofitDataSource(get(),get())
    }

    factory<ITransactionRepository> { TransactionRepository(get(),get(),get()) }


}

val useCaseModule = module {
    factory {
        GetTransactions(get())
    }

}

val viewModelModule = module {

    single<CoroutineDispatcher> { Dispatchers.Main }

    viewModel {
        MainViewModel(get())
    }
}
