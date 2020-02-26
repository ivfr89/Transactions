package com.fernandez.ivan.transactions.framework.server

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("1a30k8")
    fun getTransactions(): Call<String>
}
