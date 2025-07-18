package com.mytt.app.core.data.repository

import com.mytt.app.core.data.models.Offer

/**
 * Interface pour le dépôt des offres.
 * Définit un contrat pour la récupération des données liées aux offres.
 */
interface OffersRepository {

    /**
     * Récupère la liste de toutes les offres disponibles.
     * C'est une fonction de suspension car c'est une opération réseau.
     *
     * @return Un objet [Result] qui contient :
     *         - En cas de succès : la liste des offres (`List<Offer>`).
     *         - En cas d'échec : l'exception qui a causé le problème.
     */
    suspend fun getOffers(): Result<List<Offer>>

}