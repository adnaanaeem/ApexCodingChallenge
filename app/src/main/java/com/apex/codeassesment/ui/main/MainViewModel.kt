package com.apex.codeassesment.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.data.remote.ApiResult
import com.apex.codeassesment.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _userFlow = MutableStateFlow<ApiResult<User>>(ApiResult.Success(User()))
    val userFlow: StateFlow<ApiResult<User>> = _userFlow

    private val _userFlowList = MutableStateFlow<ApiResult<List<User>>>(ApiResult.Success(emptyList()))
    val userFlowList: StateFlow<ApiResult<List<User>>> = _userFlowList

    init {
        refreshUser(true)
    }

    fun onEvent(event: MainViewEvent) {
        viewModelScope.launch {
            when (event) {
                MainViewEvent.ComposeScreenEvent -> _uiEvent.emit(UiEvent.NavigateToCompose)
                MainViewEvent.UserListEvent -> updateUserList()
                is MainViewEvent.RefreshEvent -> refreshUser(event.force)
                is MainViewEvent.UserDetailsEvent -> _uiEvent.emit(UiEvent.NavigateToUser(event.user))
            }
        }
    }

    private fun updateUserList() {
        viewModelScope.launch {
            userRepository.getUsers()
                .onStart { _userFlow.value = ApiResult.Loading("Fetching Users List") }
                .collect {apiResult ->
                    _userFlowList.value = apiResult
                }
        }
    }

    private fun refreshUser(isUser: Boolean) {
        viewModelScope.launch {
            userRepository.getUser(isUser)
                .onStart { _userFlow.value = ApiResult.Loading("Fetching User") }
                .collect {apiResult ->
                    _userFlow.value = apiResult
                    parseUserData(apiResult)
                }
        }
    }

    private fun parseUserData(result: ApiResult<User>){
        when (result) {
            is ApiResult.Error -> Unit
            is ApiResult.Loading -> Unit
            is ApiResult.Success -> {
                _uiState.update {
                    it.copy(
                        randomUser = result.data
                    )
                }
            }
        }
    }
}

sealed class UiEvent {
    data class NavigateToUser(val user: User) : UiEvent()
    object NavigateToCompose : UiEvent()
}

sealed interface MainViewEvent {
    data class UserDetailsEvent(val user: User) : MainViewEvent
    data class RefreshEvent(val force: Boolean) : MainViewEvent
    object UserListEvent : MainViewEvent
    object ComposeScreenEvent : MainViewEvent
}

data class MainUiState(
    val randomUser: User = User(),
)