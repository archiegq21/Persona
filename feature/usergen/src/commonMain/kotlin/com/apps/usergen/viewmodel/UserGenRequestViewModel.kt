package com.apps.usergen.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.model.Gender
import com.apps.usergen.ui.request.GenUserParams
import com.apps.usergen.viewmodel.UserGenUiState.ValidatedCount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class UserGenUiState(
    val count: String = "",
    val validatedCount: ValidatedCount? = null,
    val gender: Gender? = null,
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
    private val genderState = savedStateHandle.getStateFlow(GENDER_KEY, null)

    private val generateUserState = MutableStateFlow<GenUserParams?>(null)

    val uiState: StateFlow<UserGenUiState> = combine(
        countState,
        countErrorState,
        genderState,
        generateUserState,
    ) { count, countError, gender, genUserParams ->
        UserGenUiState(
            count = count,
            validatedCount = countError,
            gender = gender,
            generateUserParams = genUserParams,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserGenUiState())

    fun setCount(count: String) {
        savedStateHandle[COUNT_KEY] = count
    }

    fun setGender(gender: Gender?) {
        savedStateHandle[GENDER_KEY] = gender
    }

    fun onCountFocused() {
        countFocused.update { true }
    }

    fun onGenerateUser(
        validatedCount: ValidatedCount?,
        gender: Gender?,
    ) {
        generateUserState.update {
            if (validatedCount !is ValidatedCount.Valid) return@update null
            GenUserParams(
                userCount = validatedCount.count,
                gender = gender,
            )
        }
    }

    fun consumeGenerateUserParams() {
        generateUserState.update { null }
    }

    companion object {
        private const val COUNT_KEY = "Count"
        private const val GENDER_KEY = "Gender"
    }
}