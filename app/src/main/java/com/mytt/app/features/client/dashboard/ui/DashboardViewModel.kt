package com.mytt.app.features.client.dashboard.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel pour le DashboardFragment.
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    // Injection du client FirebaseAuth pour accéder à l'état d'authentification actuel.
    private val auth: FirebaseAuth
) : ViewModel() {

    // LiveData pour exposer le nom de l'utilisateur à la vue.
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    /**
     * Le bloc init est exécuté une seule fois lors de la création du ViewModel.
     * C'est l'endroit idéal pour charger les données initiales.
     */
    init {
        loadUserName()
    }

    /**
     * Récupère l'utilisateur actuellement connecté et met à jour le LiveData.
     */
    private fun loadUserName() {
        // Récupère l'utilisateur courant depuis FirebaseAuth.
        val currentUser = auth.currentUser

        // Met à jour le LiveData avec le nom d'affichage de l'utilisateur,
        // ou son email s'il n'a pas de nom, ou "Utilisateur" par défaut.
        _userName.value = currentUser?.displayName ?: currentUser?.email ?: "Utilisateur"
    }
}