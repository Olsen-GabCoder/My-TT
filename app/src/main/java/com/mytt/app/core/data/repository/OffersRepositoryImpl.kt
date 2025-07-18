package com.mytt.app.features.client.offers.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.mytt.app.core.data.models.Offer
import com.mytt.app.core.data.repository.OffersRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Implémentation concrète de l'OffersRepository utilisant Cloud Firestore.
 *
 * @param firestore Une instance de FirebaseFirestore, injectée par Hilt.
 */
class OffersRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : OffersRepository {

    companion object {
        // Définit le nom de la collection dans Firestore.
        // Utiliser une constante évite les erreurs de frappe et centralise le nom.
        private const val OFFERS_COLLECTION = "offers"
    }

    override suspend fun getOffers(): Result<List<Offer>> {
        return try {
            // Exécute la requête pour récupérer tous les documents de la collection "offers".
            // .await() suspend la coroutine jusqu'à ce que la tâche soit terminée.
            val querySnapshot = firestore.collection(OFFERS_COLLECTION).get().await()

            // Mappe la liste de documents récupérés en une liste d'objets Offer.
            // Firestore s'occupe de la conversion automatiquement si les noms des champs
            // dans le document correspondent aux noms des propriétés dans la data class Offer.
            val offers = querySnapshot.toObjects(Offer::class.java)

            // Retourne un succès avec la liste des offres.
            Result.success(offers)
        } catch (e: Exception) {
            // En cas d'erreur (pas de réseau, problème de permissions, etc.),
            // on la capture et on retourne un échec contenant l'exception.
            Result.failure(e)
        }
    }
}