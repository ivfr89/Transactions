package com.fernandez.ivan.domain

sealed class Failure {
    object NetworkConnection : Failure()
    class ServerErrorCode(val code: Int): Failure()
    class ServerException(val throwable: Throwable): Failure()
    class JsonException(val body: String): Failure()

    /** * Extend this class for feature specific failures.*/
    abstract class FeatureFailure: Failure()

    class NullResult: FeatureFailure()


}