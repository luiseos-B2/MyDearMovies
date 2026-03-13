package com.example.mydearmovies.core.error

import com.example.mydearmovies.domain.result.DomainError

interface ErrorMessageResProvider {
    fun getMessageRes(error: DomainError): Int
}
