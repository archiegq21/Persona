package com.apps.usergen.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.apps.usergen.data.UserCollection
import com.apps.usergen.repository.IdGenerator
import com.apps.usergen.repository.UserCollectionRepository
import com.apps.usergen.viewmodel.UserGenUiState.ValidatedCount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
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
    val validatedCount: ValidatedCount = ValidatedCount.Empty,
    val name: String = "",
    val generateUserParams: GenUserParams? = null,
) {

    val isValid: Boolean = validatedCount.isValid

    sealed interface ValidatedCount {
        val isValid: Boolean
            get() = this is Valid

        val isError: Boolean
            get() = false

        data object Zero : ValidatedCount {
            override val isError: Boolean = true
        }
        data object Empty : ValidatedCount
        data object Invalid : ValidatedCount {
            override val isError: Boolean = true
        }
        data class Valid(val count: Int): ValidatedCount
    }

}


class UserGenRequestViewModel(
    private val idGenerator: IdGenerator,
    private val savedStateHandle: SavedStateHandle,
    private val repository: UserCollectionRepository,
) : ViewModel() {

    private val countState = savedStateHandle.getStateFlow(COUNT_KEY, "")
    private val countValidationState = countState.map {
        when {
            it.isEmpty() -> ValidatedCount.Empty
            it.toIntOrNull() == 0 -> ValidatedCount.Zero
            it.toIntOrNull() == null -> ValidatedCount.Invalid
            else -> ValidatedCount.Valid(it.toInt())
        }
    }
    private val nameState = savedStateHandle.getStateFlow(NAME_KEY, "")

    private val generateUserState = MutableStateFlow<GenUserParams?>(null)

    val uiState: StateFlow<UserGenUiState> = combine(
        countState,
        countValidationState,
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

    fun onGenerateUser(
        validatedCount: ValidatedCount,
        name: String,
    ) {
        viewModelScope.launch {
            try {
                if (validatedCount !is ValidatedCount.Valid) return@launch

                val id = idGenerator.generateId()
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
        @VisibleForTesting
        const val COUNT_KEY = "Count"

        @VisibleForTesting
        const val NAME_KEY = "Name"
    }
}