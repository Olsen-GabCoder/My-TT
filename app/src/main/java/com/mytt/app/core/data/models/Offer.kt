// CORRIGÉ : Le package est maintenant centralisé, comme défini dans notre plan d'architecture.
package com.mytt.app.core.data.models

/**
 * Classe de données représentant une offre télécom.
 * C'est un modèle de données central, potentiellement utilisé
 * par l'espace Client (pour l'affichage) et l'espace Admin (pour la gestion).
 *
 * @param id L'identifiant unique de l'offre.
 * @param name Le nom commercial de l'offre (ex: "Forfait Internet Maxi").
 * @param description Une courte description de l'offre.
 * @param price Le prix de l'offre.
 */
data class Offer(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: String = ""
)