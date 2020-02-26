package com.fernandez.ivan.transactions.framework.server

import com.fernandez.ivan.domain.Either
import com.fernandez.ivan.domain.Failure
import retrofit2.Call

interface IRequest {

    fun <T, R> request(call: Call<T>, transform: (T) -> R, default: T): Either<Failure, R>

    class RequestImplementation : IRequest{
        override fun <T, R> request(
            call: Call<T>,
            transform: (T) -> R,
            default: T
        ): Either<Failure, R> {
            return try {
                val response = call.execute()
                when (response.isSuccessful) {
                    true -> {
                        Either.Right(transform((response.body() ?: default)))
                    }
                    false -> {
                        Either.Left(Failure.ServerErrorCode(response.code()))
                    }
                }
            } catch (exception: Throwable) {
                Either.Left(Failure.ServerException(exception))
            }
        }

    }

}