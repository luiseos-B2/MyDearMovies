package com.example.mydearmovies.data.util

import com.example.mydearmovies.domain.result.DomainError
import retrofit2.HttpException
import java.io.IOException

fun Throwable.toDomainError(): DomainError = when (this) {
    is IOException -> DomainError.Network
    is HttpException -> if (code() == 404) DomainError.NotFound else DomainError.Unknown
    else -> DomainError.Unknown
}
