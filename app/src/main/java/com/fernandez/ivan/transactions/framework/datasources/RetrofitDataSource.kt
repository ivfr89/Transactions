package com.fernandez.ivan.transactions.framework.datasources

import com.fernandez.ivan.data.interfaces.IRemoteDataSource
import com.fernandez.ivan.data.models.EntityTransaction
import com.fernandez.ivan.domain.Either
import com.fernandez.ivan.domain.Failure
import com.fernandez.ivan.domain.Transaction
import com.fernandez.ivan.transactions.framework.empty
import com.fernandez.ivan.transactions.framework.server.ApiService
import com.fernandez.ivan.transactions.framework.server.IRequest
import com.fernandez.ivan.transactions.framework.server.ServerMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitDataSource(
    private val service: ApiService,
    private val serverMapper: ServerMapper):
    IRemoteDataSource, IRequest by IRequest.RequestImplementation() {

    override suspend fun getTransactions(): Either<Failure, List<Transaction>> = withContext(Dispatchers.IO) {

        request(service.getTransactions(),{
            serverMapper.parseArrayResponse<EntityTransaction>(it).map { entity-> entity.toDomain() }
        }, String.empty)
    }
}