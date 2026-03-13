package com.example.mydearmovies.data.util

import com.example.mydearmovies.domain.result.DomainError
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ErrorMapperTest {

    @Test
    fun `shouldReturnNetworkErrorWhenIOExceptionThrown`() {
        // Given
        val throwable = IOException()

        // When
        val result = throwable.toDomainError()

        // Then
        assertEquals(DomainError.Network, result)
    }

    @Test
    fun `shouldReturnNotFoundErrorWhenHttpException404Thrown`() {
        // Given
        val response = Response.error<Any>(
            404,
            ResponseBody.create("application/json".toMediaTypeOrNull(), "")
        )
        val throwable = HttpException(response)

        // When
        val result = throwable.toDomainError()

        // Then
        assertEquals(DomainError.NotFound, result)
    }

    @Test
    fun `shouldReturnUnknownErrorWhenHttpExceptionNon404Thrown`() {
        // Given
        val response = Response.error<Any>(
            500,
            ResponseBody.create("application/json".toMediaTypeOrNull(), "")
        )
        val throwable = HttpException(response)

        // When
        val result = throwable.toDomainError()

        // Then
        assertEquals(DomainError.Unknown, result)
    }

    @Test
    fun `shouldReturnUnknownErrorWhenOtherThrowableThrown`() {
        // Given
        val throwable = IllegalStateException("boom")

        // When
        val result = throwable.toDomainError()

        // Then
        assertEquals(DomainError.Unknown, result)
    }
}

