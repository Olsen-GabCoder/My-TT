// Fichier : app/src/main/java/com/mytt/app/features/client/dashboard/ui/DashboardFragment.kt

package com.mytt.app.features.client.dashboard.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mytt.app.R
import com.mytt.app.core.data.models.PromoOffer
import com.mytt.app.databinding.FragmentDashboardBinding
import com.mytt.app.features.client.dashboard.ui.adapters.DashboardActionAdapter
import com.mytt.app.features.client.dashboard.ui.adapters.PromoOfferAdapter
import com.mytt.app.features.client.dashboard.ui.models.DashboardAction
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // On appelle nos fonctions de configuration.
        setupUI() // << MODIFICATION : regroupement pour plus de clarté
        observeViewModel() // << MODIFICATION : séparation de l'observation
    }

    /**
     * NOUVEAU : Regroupe la configuration des vues statiques.
     */
    private fun setupUI() {
        setupBalanceCard()
        setupShortcutsRecyclerView()
        setupPromoOffersRecyclerView()
    }

    /**
     * NOUVEAU : Gère l'observation des données du ViewModel.
     * C'est ici que nous allons corriger la régression.
     */
    private fun observeViewModel() {
        // CORRECTION : On observe le nom de l'utilisateur et on met à jour le TextView.
        viewModel.userName.observe(viewLifecycleOwner) { name ->
            binding.textViewUserName.text = getString(R.string.dashboard_welcome_message, name)
        }

        // Ici, on pourrait aussi observer le solde pour le mettre à jour dynamiquement.
        // viewModel.balance.observe(viewLifecycleOwner) { balance ->
        //     binding.viewBalanceCardV2.textViewBalanceAmount.text = balance
        // }
    }


    /**
     * Configure la carte de solde en affichant les données.
     */
    private fun setupBalanceCard() {
        val userBalance = "0.025"
        val balanceCardBinding = binding.viewBalanceCardV2
        balanceCardBinding.textViewBalanceAmount.text = userBalance
        balanceCardBinding.fabRecharge.setOnClickListener {
            Toast.makeText(requireContext(), "Bouton Recharge rapide cliqué !", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Configure le RecyclerView pour la section "Raccourcis".
     */
    private fun setupShortcutsRecyclerView() {
        val shortcutActions = listOf(
            DashboardAction(R.string.action_recharge, R.drawable.ic_recharge, R.id.consumptionFragment),
            DashboardAction(R.string.action_buy_option, R.drawable.ic_add_shopping_cart, R.id.offersFragment),
            DashboardAction(R.string.action_sos, R.drawable.ic_sos, R.id.contactSupportFragment),
            DashboardAction(R.string.action_pay_bill, R.drawable.ic_billing, R.id.invoicesFragment)
        )
        val shortcutsAdapter = DashboardActionAdapter(shortcutActions) { destinationId ->
            findNavController().navigate(destinationId)
        }
        binding.recyclerViewShortcuts.adapter = shortcutsAdapter
    }

    /**
     * Configure le RecyclerView pour le carrousel d'offres promotionnelles.
     */
    private fun setupPromoOffersRecyclerView() {
        val promoOffers = listOf(
            PromoOffer(
                title = "Double Data",
                subtitle = "Activez l'option et profitez !",
                localImageResId = R.drawable.promo_image_1,
                destinationId = R.id.offersFragment
            ),
            PromoOffer(
                title = "Roaming Pass",
                subtitle = "Voyagez connecté à petit prix",
                localImageResId = R.drawable.promo_image_2,
                destinationId = R.id.offersFragment
            ),
            PromoOffer(
                title = "Info Service",
                subtitle = "Découvrez nos nouveaux services",
                localImageResId = R.drawable.promo_image_1,
                destinationId = null
            )
        )
        val promoOfferAdapter = PromoOfferAdapter(promoOffers) { destinationId ->
            findNavController().navigate(destinationId)
        }
        binding.recyclerViewPromoOffers.adapter = promoOfferAdapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}