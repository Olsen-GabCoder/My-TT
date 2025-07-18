package com.mytt.app.features.client.consumption.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Classe de données qui représente l'état complet de l'écran de consommation.
 * Regrouper l'état dans une seule classe évite d'avoir une multitude de LiveData
 * et facilite la gestion des mises à jour de l'UI.
 */
data class ConsumptionState(
    val mainBalance: String,
    val callBalance: String,
    val internetBalance: String
)

/**
 * ViewModel pour le ConsumptionFragment.
 */
@HiltViewModel
class ConsumptionViewModel @Inject constructor() : ViewModel() {

    // LiveData qui expose l'état complet de l'écran.
    private val _consumptionState = MutableLiveData<ConsumptionState>()
    val consumptionState: LiveData<ConsumptionState> = _consumptionState

    init {
        // Au démarrage du ViewModel, on charge les données (actuellement simulées).
        loadConsumptionData()
    }

    /**
     * Simule la récupération des données de consommation.
     * Plus tard, cette fonction appellera un "ConsumptionRepository" pour obtenir
     * les vraies données depuis Firestore.
     */
    private fun loadConsumptionData() {
        // Création d'un état avec des données en dur pour le test.
        val fakeState = ConsumptionState(
            mainBalance = "33,00 CFA",
            callBalance = "120 Mins",
            internetBalance = "2,5 Go"
        )

        // Mise à jour du LiveData avec le nouvel état.
        _consumptionState.value = fakeState
    }
}