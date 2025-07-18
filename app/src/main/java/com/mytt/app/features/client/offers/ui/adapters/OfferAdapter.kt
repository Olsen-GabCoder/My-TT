package com.mytt.app.features.client.offers.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mytt.app.core.data.models.Offer
import com.mytt.app.databinding.ItemOfferBinding

/**
 * Adapter pour le RecyclerView qui affiche la liste des offres.
 *
 * Il hérite de ListAdapter, une classe spécialisée qui gère automatiquement les mises à jour
 * de la liste de manière très performante grâce à DiffUtil.
 *
 * @param Offer Le type de données de notre liste.
 * @param OfferViewHolder Le type de ViewHolder que cet adapter utilise.
 */
class OffersAdapter : ListAdapter<Offer, OffersAdapter.OfferViewHolder>(OfferDiffCallback()) {

    /**
     * Le ViewHolder contient les références aux vues d'un seul item de la liste.
     * Cela évite de faire des appels coûteux à "findViewById" à chaque fois.
     */
    inner class OfferViewHolder(private val binding: ItemOfferBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Lie un objet Offer aux vues de cet item.
         */
        fun bind(offer: Offer) {
            binding.textViewOfferName.text = offer.name
            binding.textViewOfferDescription.text = offer.description
            binding.textViewOfferPrice.text = offer.price
        }
    }

    /**
     * Appelé par le RecyclerView quand il a besoin de créer un nouveau ViewHolder.
     * C'est ici que nous "inflons" (créons) notre layout item_offer.xml.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val binding = ItemOfferBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OfferViewHolder(binding)
    }

    /**
     * Appelé par le RecyclerView pour afficher les données à une position donnée.
     * On récupère l'objet Offer correspondant et on appelle la méthode bind du ViewHolder.
     */
    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        val offer = getItem(position)
        holder.bind(offer)
    }
}

/**
 * C'est le "cerveau" de ListAdapter. Il sait comment comparer deux listes
 * et deux items pour déterminer les changements (ajouts, suppressions, modifications).
 * Cela permet au RecyclerView de n'animer que les changements nécessaires,
 * ce qui est beaucoup plus performant que de tout rafraîchir.
 */
class OfferDiffCallback : DiffUtil.ItemCallback<Offer>() {
    override fun areItemsTheSame(oldItem: Offer, newItem: Offer): Boolean {
        // Les items sont les mêmes si leur ID est identique.
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Offer, newItem: Offer): Boolean {
        // Le contenu est le même si les objets sont égaux (data class s'en charge).
        return oldItem == newItem
    }
}