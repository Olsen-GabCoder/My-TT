package com.mytt.app.features.client.profile.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mytt.app.core.data.models.User
import com.mytt.app.core.data.repository.AuthRepository
import com.mytt.app.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // Utilise un SingleLiveEvent pour s'assurer que la navigation de déconnexion
    // n'est déclenchée qu'une seule fois.
    private val _logoutEvent = SingleLiveEvent<Unit>()
    val logoutEvent: LiveData<Unit> = _logoutEvent

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            // Nous réutilisons le AuthRepository qui est notre source de vérité.
            val result = authRepository.getCurrentUser()
            result.onSuccess { user ->
                _user.value = user
            }.onFailure { exception ->
                _error.value = exception.message ?: "Impossible de charger les données du profil."
            }
        }
    }

    fun onLogoutClicked() {
        viewModelScope.launch {
            authRepository.logout()
            // Une fois la déconnexion réussie, on notifie la vue.
            _logoutEvent.value = Unit
        }
    }
}