// Fichier : app/src/main/java/com/mytt/app/features/authentication/ui/RegistrationViewModel.kt

package com.mytt.app.features.authentication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mytt.app.core.data.models.User
import com.mytt.app.core.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RegistrationUiState {
    object Idle : RegistrationUiState()
    object Loading : RegistrationUiState()
    data class RegistrationSuccess(val user: User) : RegistrationUiState()
    data class Error(val message: String) : RegistrationUiState()
}

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<RegistrationUiState>(RegistrationUiState.Idle)
    val uiState: LiveData<RegistrationUiState> = _uiState

    fun onRegisterClicked(name: String, phoneNumber: String, email: String, password: String) {
        if (name.isBlank() || phoneNumber.isBlank() || email.isBlank() || password.isBlank()) {
            _uiState.value = RegistrationUiState.Error("Tous les champs sont obligatoires.")
            return
        }
        if (password.length < 6) {
            _uiState.value = RegistrationUiState.Error("Le mot de passe doit contenir au moins 6 caractères.")
            return
        }
        if (phoneNumber.length != 8) {
            _uiState.value = RegistrationUiState.Error("Le numéro de téléphone doit contenir 8 chiffres.")
            return
        }

        _uiState.value = RegistrationUiState.Loading
        viewModelScope.launch {
            val result = authRepository.registerUser(name, phoneNumber, email, password)
            result.onSuccess { newUser ->
                _uiState.value = RegistrationUiState.RegistrationSuccess(newUser)
            }.onFailure { exception ->
                val errorMessage = when {
                    exception.message?.contains("email address is already in use", ignoreCase = true) == true ->
                        "Cette adresse e-mail est déjà utilisée."
                    exception.message?.contains("phone number is already in use", ignoreCase = true) == true ->
                        "Ce numéro de téléphone est déjà utilisé."
                    else ->
                        exception.message ?: "Une erreur est survenue lors de l'inscription."
                }
                _uiState.value = RegistrationUiState.Error(errorMessage)
            }
        }
    }

    fun resetState() {
        _uiState.value = RegistrationUiState.Idle
    }
}