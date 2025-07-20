// Fichier : app/src/main/java/com/mytt/app/core/data/models/PromotionItem.kt

package com.mytt.app.core.data.models

import androidx.annotation.DrawableRes
import com.mytt.app.features.client.dashboard.ui.models.DashboardAction

/**
 * Représente un item dans la section "Promotions & Avantages".
 * Une classe scellée (sealed class) est parfaite ici car elle nous permet de définir un ensemble
 * fixe de types possibles pour un item de cette liste. Un item ne peut être
 * qu'un 'FeaturedOffer' ou un 'StandardAction'.
 * Placé dans 'core' pour respecter l'architecture du projet.
 */
sealed class PromotionItem {

    /**
     * Représente la grande carte "en vedette".
     */
    data class FeaturedOffer(
        val title: String,
        val subtitle: String,
        @DrawableRes val imageResId: Int,
        val destinationId: Int? = null
    ) : PromotionItem()

    /**
     * Représente une carte d'action standard, comme "Promos" ou "Roue de la chance".
     * Elle encapsule simplement notre modèle existant 'DashboardAction'.
     *
     * IMPORTANT : L'import de 'DashboardAction' doit pointer vers son emplacement réel.
     * Si 'DashboardAction' est dans le package '...dashboard.ui.models', cet import est correct.
     * S'il est aussi dans 'core', l'import doit être ajusté.
     */
    data class StandardAction(
        val action: DashboardAction
    ) : PromotionItem()
}