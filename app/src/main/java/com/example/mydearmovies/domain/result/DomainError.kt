package com.example.mydearmovies.domain.result

sealed class DomainError {
    data object Network : DomainError()
    data object NotFound : DomainError()
    data object InvalidParams : DomainError()
    data object Unknown : DomainError()
}
