package com.example.mydearmovies.feature.media.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydearmovies.domain.model.MediaDetailModel
import com.example.mydearmovies.domain.model.MediaType
import com.example.mydearmovies.domain.result.DomainError
import com.example.mydearmovies.domain.result.fold
import com.example.mydearmovies.domain.usecase.GetMediaDetailsUseCase
import com.example.mydearmovies.core.error.ErrorMessageResProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaDetailsViewModel @Inject constructor(
    private val getMediaDetailsUseCase: GetMediaDetailsUseCase,
    private val errorMessageResProvider: ErrorMessageResProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow<MediaDetailsState>(MediaDetailsState.Loading)
    val state = _state.asStateFlow()

    init {
        val mediaTypeStr = savedStateHandle.get<String>("mediaType")
        val mediaId = savedStateHandle.get<String>("mediaId")?.toIntOrNull()
        val type = when (mediaTypeStr?.uppercase()) {
            "MOVIE" -> MediaType.MOVIE
            "TV" -> MediaType.TV
            else -> null
        }
        if (type != null && mediaId != null) load(type, mediaId)
        else _state.update { MediaDetailsState.Error(errorMessageResProvider.getMessageRes(DomainError.InvalidParams)) }
    }

    fun load(mediaType: MediaType, mediaId: Int) {
        viewModelScope.launch {
            _state.update { MediaDetailsState.Loading }
            getMediaDetailsUseCase(mediaType, mediaId).collect { result ->
                result.fold(
                    onSuccess = { detail ->
                        _state.update { MediaDetailsState.Success(detail) }
                    },
                    onFailure = { error ->
                        _state.update { MediaDetailsState.Error(errorMessageResProvider.getMessageRes(error)) }
                    }
                )
            }
        }
    }
}

sealed class MediaDetailsState {
    data object Loading : MediaDetailsState()
    data class Success(val detail: MediaDetailModel) : MediaDetailsState()
    data class Error(val errorMessageRes: Int) : MediaDetailsState()
}
