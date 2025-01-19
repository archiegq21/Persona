package com.apps.usergen.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.apps.model.Gender
import com.apps.usergen.data.UserCollection
import com.apps.usergen.repository.UserCollectionRepository
import com.apps.usergen.viewmodel.UserGenUiState.ValidatedCount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class GenUserParams(
    val id: String,
    val name: String,
)

data class UserGenUiState(
    val count: String = "",
    val validatedCount: ValidatedCount? = null,
    val name: String = "",
    val generateUserParams: GenUserParams? = null,
) {

    val isFormValid: Boolean
        get() = validatedCount is ValidatedCount.Valid

    sealed interface ValidatedCount {
        val isError: Boolean
            get() = this !is Valid

        data class Valid(val count: Int) : ValidatedCount
        data object Zero : ValidatedCount
        data object Invalid : ValidatedCount
    }

}


class UserGenRequestViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: UserCollectionRepository,
) : ViewModel() {

    private val countState = savedStateHandle.getStateFlow(COUNT_KEY, "")
    private val countFocused = MutableStateFlow(false)
    private val countErrorState = combine(
        countState.map { it.toIntOrNull() }.distinctUntilChanged(),
        countFocused,
    ) { count, focused ->
        if (!focused && count != null) return@combine null
        when (count) {
            null -> ValidatedCount.Invalid
            0 -> ValidatedCount.Zero
            else -> ValidatedCount.Valid(count)
        }
    }
    private val nameState = savedStateHandle.getStateFlow(NAME_KEY, "")

    private val generateUserState = MutableStateFlow<GenUserParams?>(null)

    val uiState: StateFlow<UserGenUiState> = combine(
        countState,
        countErrorState,
        nameState,
        generateUserState,
    ) { count, countError, name, genUserParams ->
        UserGenUiState(
            count = count,
            validatedCount = countError,
            name = name,
            generateUserParams = genUserParams,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserGenUiState())

    fun setCount(count: String) {
        savedStateHandle[COUNT_KEY] = count
    }

    fun setName(name: String) {
        savedStateHandle[NAME_KEY] = name
    }

    fun onCountFocused() {
        countFocused.update { true }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun onGenerateUser(
        validatedCount: ValidatedCount?,
        name: String,
    ) {
        viewModelScope.launch {
            try {
                if (validatedCount !is ValidatedCount.Valid) return@launch

                val id = Uuid.random().toHexString()
                val userCollection = UserCollection(
                    id = id,
                    count = validatedCount.count,
                    name = name,
                )

                repository.addUserCollection(userCollection)

                generateUserState.update {
                    GenUserParams(
                        id = id,
                        name = name,
                    )
                }
            } catch (e: Exception) {
                Logger.e("Error", e, tag = "Error")
            }
        }
    }

    fun consumeGenerateUserParams() {
        generateUserState.update { null }
    }

    companion object {
        private const val COUNT_KEY = "Count"
        private const val NAME_KEY = "Name"
    }
}