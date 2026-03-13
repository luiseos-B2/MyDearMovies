package com.example.mydearmovies.core.error

import com.example.mydearmovies.R
import com.example.mydearmovies.domain.result.DomainError
import javax.inject.Inject

class AndroidErrorMessageResProvider @Inject constructor() : ErrorMessageResProvider {

    override fun getMessageRes(error: DomainError): Int = when (error) {
        is DomainError.Network -> R.string.error_network
        is DomainError.NotFound -> R.string.error_not_found
        is DomainError.InvalidParams -> R.string.error_invalid_params
        is DomainError.Unknown -> R.string.error_generic
    }
}
