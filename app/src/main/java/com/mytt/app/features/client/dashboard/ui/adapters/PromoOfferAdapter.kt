// Fichier : app/src/main/java/com/mytt/app/features/client/dashboard/ui/adapters/PromoOfferAdapter.kt

package com.mytt.app.features.client.dashboard.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mytt.app.core.data.models.PromoOffer
import com.mytt.app.databinding.ItemPromoOfferBinding

/**
 * Adaptateur pour afficher une liste d'objets [PromoOffer] dans le carrousel du dashboard.
 *
 * @property offers La liste des offres à afficher.
 * @property onItemClick Une fonction lambda qui sera appelée lorsqu'une offre est cliquée,
 *                       passant l'ID de la destination en paramètre.
 */
class PromoOfferAdapter(
    private val offers: List<PromoOffer>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<PromoOfferAdapter.PromoOfferViewHolder>() {

    /**
     * Le ViewHolder pour une carte d'offre.
     */
    inner class PromoOfferViewHolder(private val binding: ItemPromoOfferBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Lie les données d'une offre spécifique aux vues de ce ViewHolder.
         * @param offer L'objet PromoOffer à afficher.
         */
        fun bind(offer: PromoOffer) {
            binding.textViewOfferTitle.text = offer.title
            binding.textViewOfferSubtitle.text = offer.subtitle

            // Gère l'affichage de l'image (locale pour l'instant)
            offer.localImageResId?.let { imageRes ->
                binding.imageViewOfferBackground.setImageResource(imageRes)
            }
            // Note : Pour charger depuis une URL, on utiliserait Glide ou Coil ici.

            // Gère la cliquabilité de la carte
            offer.destinationId?.let { destId ->
                binding.root.setOnClickListener {
                    onItemClick(destId)
                }
            } ?: run {
                // Si destinationId est null, la carte n'est pas cliquable.
                binding.root.isClickable = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromoOfferViewHolder {
        val binding = ItemPromoOfferBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PromoOfferViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PromoOfferViewHolder, position: Int) {
        holder.bind(offers[position])
    }

    override fun getItemCount(): Int {
        return offers.size
    }
}