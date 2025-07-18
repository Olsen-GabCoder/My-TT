// Fichier : app/src/main/java/com/mytt/app/core/data/models/PromoOffer.kt

package com.mytt.app.core.data.models

import androidx.annotation.DrawableRes

/**
 * Représente une offre promotionnelle.
 * Ce modèle est placé dans le module 'core' car il peut être réutilisé
 * par différentes fonctionnalités (dashboard, écran des offres, etc.).
 *
 * @property title Le titre principal de l'offre (ex: "Double Data").
 * @property subtitle Le sous-titre ou l'appel à l'action (ex: "Activez l'option et profitez !").
 * @property imageUrl L'URL de l'image de fond. Pour une application réelle, cette image
 *                    viendrait d'un serveur (Firebase Storage, etc.).
 * @property localImageResId Un ID de ressource drawable local pour le prototypage.
 *                         Nous l'utiliserons pour afficher des images statiques de l'app.
 * @property destinationId L'ID de la destination dans le graphe de navigation si l'offre est cliquable.
 *                         Utiliser une valeur de 0 ou -1 pour indiquer une offre non cliquable.
 */
data class PromoOffer(
    val title: String,
    val subtitle: String,
    val imageUrl: String? = null,
    @DrawableRes val localImageResId: Int? = null, // Idéal pour le prototypage
    val destinationId: Int? = null
)