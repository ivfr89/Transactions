package com.fernandez.ivan.usecases

import com.fernandez.ivan.domain.Either
import com.fernandez.ivan.domain.Failure

abstract class UseCase<in Params,out Type> where Type : Any {

    abstract suspend fun run(params: Params): Either<Failure, Type>

    suspend fun execute(params: Params): Either<Failure, Type> = run(params)

}
