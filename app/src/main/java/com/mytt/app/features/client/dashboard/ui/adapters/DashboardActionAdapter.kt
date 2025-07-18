// Fichier : app/src/main/java/com/mytt/app/features/client/dashboard/ui/adapters/DashboardActionAdapter.kt

package com.mytt.app.features.client.dashboard.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mytt.app.databinding.ItemDashboardActionBinding
import com.mytt.app.features.client.dashboard.ui.models.DashboardAction

/**
 * Adaptateur pour afficher une liste d'objets [DashboardAction] dans un RecyclerView.
 *
 * @property actions La liste des actions à afficher.
 * @property onItemClick Une fonction lambda qui sera appelée lorsqu'un item est cliqué,
 *                       passant l'ID de la destination en paramètre.
 */
class DashboardActionAdapter(
    private val actions: List<DashboardAction>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<DashboardActionAdapter.ActionViewHolder>() {

    /**
     * Le ViewHolder contient les références aux vues de l'item layout.
     * Il évite les appels répétitifs à `findViewById`.
     */
    inner class ActionViewHolder(private val binding: ItemDashboardActionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            // Configure le listener de clic une seule fois, à la création du ViewHolder.
            binding.root.setOnClickListener {
                // Récupère la position de l'item cliqué.
                val position = adapterPosition
                // Vérifie que la position est valide avant de déclencher l'action.
                if (position != RecyclerView.NO_POSITION) {
                    val clickedAction = actions[position]
                    onItemClick(clickedAction.destinationId)
                }
            }
        }

        /**
         * Lie les données d'une action spécifique aux vues de ce ViewHolder.
         * @param action L'objet DashboardAction à afficher.
         */
        fun bind(action: DashboardAction) {
            binding.textViewActionTitle.setText(action.titleResId)
            binding.imageViewActionIcon.setImageResource(action.iconResId)
        }
    }

    /**
     * Appelé par le RecyclerView pour créer un nouveau ViewHolder.
     * Il "gonfle" (inflate) le layout XML de l'item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionViewHolder {
        // Utilise le ViewBinding pour créer une instance du layout de l'item. C'est la méthode moderne.
        val binding = ItemDashboardActionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ActionViewHolder(binding)
    }

    /**
     * Appelé par le RecyclerView pour afficher les données à une position spécifique.
     * Il met à jour le contenu du ViewHolder pour refléter l'item à cette position.
     */
    override fun onBindViewHolder(holder: ActionViewHolder, position: Int) {
        val action = actions[position]
        holder.bind(action)
    }

    /**
     * Retourne le nombre total d'items dans la liste.
     * Le RecyclerView a besoin de cette information pour savoir combien d'items afficher.
     */
    override fun getItemCount(): Int {
        return actions.size
    }
}