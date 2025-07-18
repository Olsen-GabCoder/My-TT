// Fichier : app/src/main/java/com/mytt/app/features/client/dashboard/ui/models/DashboardAction.kt

package com.mytt.app.features.client.dashboard.ui.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Représente une action cliquable sur le tableau de bord.
 * C'est une classe de données (data class) immuable, ce qui est une bonne pratique pour les modèles
 * utilisés dans des listes ou des adaptateurs.
 *
 * @property titleResId L'ID de la ressource String pour le titre de l'action (ex: R.string.recharge).
 *                      Utiliser un ID de ressource garantit la prise en charge multilingue.
 * @property iconResId L'ID de la ressource Drawable pour l'icône de l'action (ex: R.drawable.ic_recharge).
 * @property destinationId L'ID de la destination dans le graphe de navigation (ex: R.id.consumptionFragment)
 *                         ou une action de navigation (ex: R.id.action_dashboard_to_...).
 *                         Permet de rendre l'action réellement cliquable.
 */
data class DashboardAction(
    @StringRes val titleResId: Int,
    @DrawableRes val iconResId: Int,
    val destinationId: Int
)