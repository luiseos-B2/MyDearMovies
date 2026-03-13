package com.example.mydearmovies.domain.result

sealed class DomainResult<out T> {
    data class Success<T>(val data: T) : DomainResult<T>()
    data class Failure(val error: DomainError) : DomainResult<Nothing>()
}

inline fun <T> DomainResult<T>.fold(
    onSuccess: (T) -> Unit,
    onFailure: (DomainError) -> Unit
) {
    when (this) {
        is DomainResult.Success -> onSuccess(data)
        is DomainResult.Failure -> onFailure(error)
    }
}

fun <T, R> DomainResult<T>.map(transform: (T) -> R): DomainResult<R> =
    when (this) {
        is DomainResult.Success -> DomainResult.Success(transform(data))
        is DomainResult.Failure -> DomainResult.Failure(error)
    }

fun <T> DomainResult<T>.getOrNull(): T? = when (this) {
    is DomainResult.Success -> data
    is DomainResult.Failure -> null
}
