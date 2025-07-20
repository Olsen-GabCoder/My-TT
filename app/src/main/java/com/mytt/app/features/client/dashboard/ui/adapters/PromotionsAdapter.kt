// Fichier : app/src/main/java/com/mytt/app/features/client/dashboard/ui/adapters/PromotionsAdapter.kt

package com.mytt.app.features.client.dashboard.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.mytt.app.core.data.models.PromotionItem
import com.mytt.app.databinding.ItemDashboardActionBinding
import com.mytt.app.databinding.ItemFeaturedOfferBinding

/**
 * Adaptateur pour la section "Promotions & Avantages", capable de gérer plusieurs types de vues.
 */
class PromotionsAdapter(
    private val items: List<PromotionItem>,
    private val onItemClick: (Int?) -> Unit
) : RecyclerView.Adapter<PromotionsAdapter.PromotionViewHolder>() {

    // Constantes pour les types de vues. C'est une bonne pratique pour la lisibilité.
    companion object {
        private const val VIEW_TYPE_FEATURED = 1
        private const val VIEW_TYPE_STANDARD = 2
    }

    /**
     * Le ViewHolder de base. Il est 'scellé' (sealed) pour correspondre à notre modèle de données.
     * Chaque sous-classe de ViewHolder saura comment lier son type de vue spécifique.
     */
    sealed class PromotionViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

        class FeaturedViewHolder(private val binding: ItemFeaturedOfferBinding) : PromotionViewHolder(binding) {
            fun bind(item: PromotionItem.FeaturedOffer, onItemClick: (Int?) -> Unit) {
                binding.textViewFeaturedTitle.text = item.title
                binding.textViewFeaturedSubtitle.text = item.subtitle
                binding.imageViewFeaturedBackground.setImageResource(item.imageResId)
                binding.root.setOnClickListener { onItemClick(item.destinationId) }
            }
        }

        class StandardViewHolder(private val binding: ItemDashboardActionBinding) : PromotionViewHolder(binding) {
            fun bind(item: PromotionItem.StandardAction, onItemClick: (Int?) -> Unit) {
                binding.textViewActionTitle.setText(item.action.titleResId)
                binding.imageViewActionIcon.setImageResource(item.action.iconResId)
                binding.root.setOnClickListener { onItemClick(item.action.destinationId) }
            }
        }
    }

    /**
     * ÉTAPE CLÉ 1 : Déterminer quel type de vue utiliser pour un item à une position donnée.
     * C'est ici que la classe scellée brille.
     */
    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is PromotionItem.FeaturedOffer -> VIEW_TYPE_FEATURED
            is PromotionItem.StandardAction -> VIEW_TYPE_STANDARD
        }
    }

    /**
     * ÉTAPE CLÉ 2 : Créer le bon ViewHolder pour le bon type de vue.
     * Le paramètre 'viewType' vient directement de la méthode getItemViewType.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromotionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_FEATURED -> {
                val binding = ItemFeaturedOfferBinding.inflate(inflater, parent, false)
                PromotionViewHolder.FeaturedViewHolder(binding)
            }
            VIEW_TYPE_STANDARD -> {
                val binding = ItemDashboardActionBinding.inflate(inflater, parent, false)
                PromotionViewHolder.StandardViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    /**
     * ÉTAPE CLÉ 3 : Lier les données au bon ViewHolder.
     * Le 'when' garantit que nous appelons la bonne méthode 'bind' pour le bon type de ViewHolder.
     */
    override fun onBindViewHolder(holder: PromotionViewHolder, position: Int) {
        val currentItem = items[position]
        when (holder) {
            is PromotionViewHolder.FeaturedViewHolder -> holder.bind(currentItem as PromotionItem.FeaturedOffer, onItemClick)
            is PromotionViewHolder.StandardViewHolder -> holder.bind(currentItem as PromotionItem.StandardAction, onItemClick)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}