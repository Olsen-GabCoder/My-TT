// Fichier : app/src/main/java/com/mytt/app/features/authentication/ui/OtpEntryViewModel.kt

package com.mytt.app.features.authentication.ui

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mytt.app.core.data.models.User
import com.mytt.app.core.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Les états de l'UI pour l'écran de vérification de l'OTP.
 */
sealed class OtpAuthUiState {
    object Idle : OtpAuthUiState()
    object Loading : OtpAuthUiState()
    data class VerificationSuccess(val user: User) : OtpAuthUiState()
    data class Error(val message: String) : OtpAuthUiState()
}

@HiltViewModel
class OtpEntryViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // LiveData pour l'état principal de l'UI (chargement, succès, erreur)
    private val _uiState = MutableLiveData<OtpAuthUiState>(OtpAuthUiState.Idle)
    val uiState: LiveData<OtpAuthUiState> = _uiState

    // LiveData spécifiques pour le timer de renvoi
    private val _timerText = MutableLiveData<String>()
    val timerText: LiveData<String> = _timerText

    private val _isResendEnabled = MutableLiveData<Boolean>(false)
    val isResendEnabled: LiveData<Boolean> = _isResendEnabled

    private var timer: CountDownTimer? = null

    init {
        startTimer()
    }

    /**
     * Démarre un compte à rebours de 60 secondes.
     */
    fun startTimer() {
        _isResendEnabled.value = false
        timer?.cancel() // Annule le timer précédent s'il existe
        timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timerText.value = "Renvoyer le code dans ${millisUntilFinished / 1000}s"
            }

            override fun onFinish() {
                _timerText.value = ""
                _isResendEnabled.value = true
            }
        }.start()
    }

    /**
     * Vérifie le code OTP saisi par l'utilisateur.
     * @param verificationId L'ID de session reçu de Firebase lors de l'envoi du code.
     * @param otpCode Le code à 6 chiffres.
     */
    fun verifyOtp(verificationId: String, otpCode: String) {
        if (otpCode.length < 6) {
            _uiState.value = OtpAuthUiState.Error("Le code doit contenir 6 chiffres.")
            return
        }

        _uiState.value = OtpAuthUiState.Loading
        viewModelScope.launch {
            val result = authRepository.signInWithPhoneAuthCredential(verificationId, otpCode)
            result.onSuccess { user ->
                _uiState.value = OtpAuthUiState.VerificationSuccess(user)
            }.onFailure { exception ->
                _uiState.value = OtpAuthUiState.Error(exception.message ?: "Code de vérification invalide.")
            }
        }
    }

    /**
     * Réinitialise l'état de l'UI à Idle.
     */
    fun resetState() {
        _uiState.value = OtpAuthUiState.Idle
    }

    // S'assure que le timer est annulé lorsque le ViewModel est détruit pour éviter les fuites mémoire.
    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }
}