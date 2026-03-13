package com.example.mydearmovies.feature.people.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mydearmovies.domain.model.PersonModel
import com.example.mydearmovies.domain.usecase.GetPopularPeopleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PeopleViewModel @Inject constructor(
    private val getPopularPeopleUseCase: GetPopularPeopleUseCase
) : ViewModel() {

    val pagingData: Flow<PagingData<PersonModel>> =
        getPopularPeopleUseCase()
            .cachedIn(viewModelScope)
}
