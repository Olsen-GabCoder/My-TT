package com.mytt.app.features.authentication.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mytt.app.core.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorEvent = MutableLiveData<String>()
    val errorEvent: LiveData<String> = _errorEvent

    // MODIFIÉ : Renommé pour être plus générique.
    private val _navigateToClientAreaEvent = MutableLiveData<Boolean>()
    val navigateToClientAreaEvent: LiveData<Boolean> = _navigateToClientAreaEvent

    // NOUVEAU : LiveData pour signaler la navigation vers l'espace admin.
    private val _navigateToAdminAreaEvent = MutableLiveData<Boolean>()
    val navigateToAdminAreaEvent: LiveData<Boolean> = _navigateToAdminAreaEvent

    fun onLoginClicked(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authRepository.login(email, password)
            _isLoading.value = false

            // MODIFIÉ : Gestion du nouvel objet User.
            result.onSuccess { user ->
                Log.d("LoginViewModel", "Login SUCCESS! User role: ${user.role}")
                // Aiguillage en fonction du rôle de l'utilisateur.
                when (user.role) {
                    "admin" -> _navigateToAdminAreaEvent.value = true
                    else -> _navigateToClientAreaEvent.value = true // "client" ou tout autre rôle par défaut.
                }
            }.onFailure { exception ->
                Log.e("LoginViewModel", "Login FAILED: ${exception.message}")
                exception.message?.let {
                    _errorEvent.value = it
                }
            }
        }
    }

    /**
     * MODIFIÉ : Renommé pour être plus explicite.
     */
    fun onClientNavigationDone() {
        _navigateToClientAreaEvent.value = false
    }

    /**
     * NOUVEAU : Fonction pour réinitialiser l'événement de navigation admin.
     */
    fun onAdminNavigationDone() {
        _navigateToAdminAreaEvent.value = false
    }
}