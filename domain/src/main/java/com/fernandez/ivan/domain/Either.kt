package com.fernandez.ivan.domain


sealed class Either<out L, out R> {
    /** * Represents the left side of [Either] class which by convention is a "Failure". */
    data class Left<out L>(val a: L) : Either<L, Nothing>()
    /** * Represents the right side of [Either] class which by convention is a "Success". */
    data class Right<out R>(val b: R) : Either<Nothing, R>()

    val isRht get() = this is Right<R>
    val isLeft get() = this is Left<L>

    fun <L> left(a: L) = Left(a)
    fun <R> right(b: R) = Right(b)

    suspend fun fold(fnL: suspend (L) -> Any, fnR: suspend (R) -> Any): Any =
        when (this) {
            is Left -> fnL(a)
            is Right -> fnR(b)
        }


}

suspend fun <E, V, V2> Either<E, V>.map(f: suspend (V) -> V2): Either<E, V2> = when(this) {
    is Either.Left -> this
    is Either.Right -> Either.Right(f(this.b))
}