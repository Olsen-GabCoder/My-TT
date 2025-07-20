// Fichier : app/src/main/java/com/mytt/app/features/client/dashboard/ui/DashboardViewModel.kt

package com.mytt.app.features.client.dashboard.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.mytt.app.core.data.models.User
import com.mytt.app.core.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // Le AuthStateListener qui réagira aux changements de connexion.
    private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
        if (auth.currentUser != null) {
            // Un utilisateur est détecté comme étant connecté.
            // On lance la récupération de son profil Firestore.
            loadCurrentUser()
        } else {
            // Aucun utilisateur n'est connecté.
            _error.value = "Utilisateur déconnecté."
        }
    }

    init {
        // Attache l'écouteur. Il sera appelé immédiatement avec l'état actuel,
        // puis à chaque changement d'état.
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            val result = authRepository.getCurrentUser()
            result.onSuccess { user ->
                _user.value = user
            }.onFailure { exception ->
                _error.value = exception.message ?: "Impossible de charger les informations de l'utilisateur."
            }
        }
    }

    // Il est crucial de détacher le listener lorsque le ViewModel est détruit
    // pour éviter les fuites de mémoire.
    override fun onCleared() {
        super.onCleared()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }
}