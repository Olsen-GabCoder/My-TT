package com.mytt.app.features.client.offers.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mytt.app.core.data.models.Offer
import com.mytt.app.core.data.repository.OffersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel pour le OffersFragment.
 */
@HiltViewModel
class OffersViewModel @Inject constructor(
    // NOUVEAU : Injection de notre OffersRepository.
    private val offersRepository: OffersRepository
) : ViewModel() {

    private val _offers = MutableLiveData<List<Offer>>()
    val offers: LiveData<List<Offer>> = _offers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // NOUVEAU : LiveData pour les erreurs (bonne pratique).
    private val _errorEvent = MutableLiveData<String>()
    val errorEvent: LiveData<String> = _errorEvent

    init {
        loadOffers()
    }

    /**
     * MODIFIÉ : Récupère la liste des offres depuis le repository.
     */
    private fun loadOffers() {
        // Lancement d'une coroutine pour l'appel réseau.
        viewModelScope.launch {
            _isLoading.value = true

            // Appel de la fonction suspendue du repository.
            val result = offersRepository.getOffers()

            _isLoading.value = false

            // Gestion du résultat.
            result.onSuccess { offerList ->
                // En cas de succès, on met à jour le LiveData avec la liste reçue.
                _offers.value = offerList
                Log.d("OffersViewModel", "Successfully loaded ${offerList.size} offers.")
            }.onFailure { exception ->
                // En cas d'échec, on poste un événement d'erreur.
                _errorEvent.value = "Impossible de charger les offres."
                Log.e("OffersViewModel", "Error loading offers: ${exception.message}")
            }
        }
    }
}