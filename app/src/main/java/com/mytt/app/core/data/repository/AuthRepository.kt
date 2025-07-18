package com.mytt.app.core.data.repository

import com.mytt.app.core.data.models.User

/**
 * Interface pour le dépôt d'authentification.
 */
interface AuthRepository {

    /**
     * Tente de connecter un utilisateur et de récupérer ses informations.
     * MODIFIÉ : Retourne maintenant un objet User complet en cas de succès.
     *
     * @return Un objet [Result] qui encapsule :
     *         - [Result.success(User)] si la connexion et la récupération ont réussi.
     *         - [Result.failure(Exception)] si l'une des étapes a échoué.
     */
    suspend fun login(email: String, password: String): Result<User>

}