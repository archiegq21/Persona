package com.apps.usergen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.usergen.data.User
import com.apps.usergen.repository.UserRepository
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

interface UserState {
    object Loading: UserState
    data class Found(val user: User): UserState

    companion object {
        operator fun invoke(user: User?): UserState = when (user) {
            null -> Loading
            else -> Found(user)
        }
    }
}

class UserDetailViewModel(
    private val repository: UserRepository,
): ViewModel() {

    fun getUser(userId: String): StateFlow<UserState> =
        repository.getUserBy(userId)
            .map { UserState(it) }
            .stateIn(viewModelScope, SharingStarted.Lazily, UserState.Loading)

}