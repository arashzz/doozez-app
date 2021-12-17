package com.doozez.doozez.framework.presentation.invitation

import androidx.lifecycle.*
import com.doozez.doozez.api.PaginatedListResponse
import com.doozez.doozez.business.domain.models.invitation.Invitation
import com.doozez.doozez.business.domain.state.DataState
import com.doozez.doozez.business.interactors.invitation.GetInvitations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class InvitationListViewModel
@Inject
constructor(
    private val getInvitations: GetInvitations,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _forceUpdate = MutableLiveData<Boolean>(false)

    private val _dataState: MutableLiveData<DataState<PaginatedListResponse<Invitation>>> = MutableLiveData()
    val dataState: LiveData<DataState<PaginatedListResponse<Invitation>>> = _dataState

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _noTasksLabel = MutableLiveData<Int>()
    val noTasksLabel: LiveData<Int> = _noTasksLabel

    private val _noTaskIconRes = MutableLiveData<Int>()
    val noTaskIconRes: LiveData<Int> = _noTaskIconRes

    init {
        // Set initial state
//        setFiltering(getSavedFilterType())
        loadInvitations(true)
    }

    private fun loadInvitations(forceUpdate: Boolean) {
        _forceUpdate.value = forceUpdate
        // temporary
        // move to use forceUpdate to automatically refresh data
        viewModelScope.launch {
            getInvitations.execute()
                .onEach {
                    _dataState.value = it
                }.launchIn(viewModelScope)
        }
    }


//    fun setStateEvent(mainStateEvent: _InvitationStateEvent){
//        viewModelScope.launch {
//            when(mainStateEvent){
//                is GetAll -> {
//                    getAllInvitations.execute()
//                        .onEach {dataState ->
//                            _dataState.value = dataState
//                        }
//                        .launchIn(viewModelScope)
//                }
//            }
//        }
//    }

}


