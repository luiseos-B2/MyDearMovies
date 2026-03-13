package com.example.mydearmovies.feature.people.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydearmovies.domain.model.PersonDetailModel
import com.example.mydearmovies.domain.result.DomainError
import com.example.mydearmovies.domain.result.fold
import com.example.mydearmovies.domain.usecase.GetPersonDetailsUseCase
import com.example.mydearmovies.core.error.ErrorMessageResProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonBiographyViewModel @Inject constructor(
    private val getPersonDetailsUseCase: GetPersonDetailsUseCase,
    private val errorMessageResProvider: ErrorMessageResProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow<PersonBiographyState>(PersonBiographyState.Loading)
    val state = _state.asStateFlow()

    init {
        val personId = savedStateHandle.get<String>("personId")?.toIntOrNull()
        if (personId != null) load(personId)
        else _state.update { PersonBiographyState.Error(errorMessageResProvider.getMessageRes(DomainError.InvalidParams)) }
    }

    fun load(personId: Int) {
        viewModelScope.launch {
            _state.update { PersonBiographyState.Loading }
            getPersonDetailsUseCase(personId).collect { result ->
                result.fold(
                    onSuccess = { detail: PersonDetailModel ->
                        _state.update {
                            PersonBiographyState.Success(
                                name = detail.name,
                                biography = detail.biography
                            )
                        }
                    },
                    onFailure = { error: DomainError ->
                        _state.update { PersonBiographyState.Error(errorMessageResProvider.getMessageRes(error)) }
                    }
                )
            }
        }
    }
}

sealed class PersonBiographyState {
    data object Loading : PersonBiographyState()
    data class Success(val name: String, val biography: String) : PersonBiographyState()
    data class Error(val errorMessageRes: Int) : PersonBiographyState()
}
