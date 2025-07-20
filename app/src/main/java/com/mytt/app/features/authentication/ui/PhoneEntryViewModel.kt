// Fichier : app/src/main/java/com/mytt/app/features/authentication/ui/PhoneEntryViewModel.kt

package com.mytt.app.features.authentication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.mytt.app.core.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class PhoneAuthUiState {
    object Idle : PhoneAuthUiState()
    object Loading : PhoneAuthUiState()
    data class CodeSent(val verificationId: String) : PhoneAuthUiState()
    data class Error(val message: String) : PhoneAuthUiState()
}

@HiltViewModel
class PhoneEntryViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<PhoneAuthUiState>(PhoneAuthUiState.Idle)
    val uiState: LiveData<PhoneAuthUiState> = _uiState

    /**
     * MODIFIÉ : Prend maintenant le numéro national (8 chiffres) en paramètre.
     * Démarre le processus de vérification UNIQUEMENT si le numéro existe dans Firestore.
     */
    fun sendOtp(nationalPhoneNumber: String, activity: AuthActivity) {
        _uiState.value = PhoneAuthUiState.Loading

        viewModelScope.launch {
            // Étape 1 : Vérifier si le numéro existe dans notre base de données.
            val checkResult = authRepository.checkIfPhoneNumberExists(nationalPhoneNumber)

            checkResult.onSuccess { numberExists ->
                if (numberExists) {
                    // Étape 2 : Si le numéro existe, procéder à l'envoi de l'OTP.
                    val fullPhoneNumber = "+216$nationalPhoneNumber"
                    val callbacks = createVerificationCallbacks()
                    authRepository.startPhoneNumberVerification(fullPhoneNumber, activity, callbacks)
                } else {
                    // Si le numéro n'existe pas, afficher une erreur claire.
                    _uiState.value = PhoneAuthUiState.Error("Ce numéro n’est pas reconnu comme client Tunisie Télécom.")
                }
            }.onFailure { exception ->
                _uiState.value = PhoneAuthUiState.Error("Erreur de vérification : ${exception.message}")
            }
        }
    }

    /**
     * Crée l'objet de callbacks pour Firebase.
     */
    private fun createVerificationCallbacks(): PhoneAuthProvider.OnVerificationStateChangedCallbacks {
        return object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // Géré plus tard
            }

            override fun onVerificationFailed(e: FirebaseException) {
                _uiState.value = PhoneAuthUiState.Error("Échec de la vérification : ${e.message}")
            }



            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                _uiState.value = PhoneAuthUiState.CodeSent(verificationId)
            }
        }
    }

    fun resetState() {
        _uiState.value = PhoneAuthUiState.Idle
    }
}