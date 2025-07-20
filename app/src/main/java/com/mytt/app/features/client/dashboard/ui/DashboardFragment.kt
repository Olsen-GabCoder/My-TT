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
import com.mytt.app.core.data.models.PromotionItem
import com.mytt.app.core.data.models.User
import com.mytt.app.databinding.FragmentDashboardBinding
import com.mytt.app.features.client.dashboard.ui.adapters.DashboardActionAdapter
import com.mytt.app.features.client.dashboard.ui.adapters.PromoOfferAdapter
import com.mytt.app.features.client.dashboard.ui.adapters.PromotionsAdapter
import com.mytt.app.features.client.dashboard.ui.models.DashboardAction
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.Locale

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
        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        setupClickListeners()
        setupShortcutsRecyclerView()
        setupPromotionsRecyclerView()
        setupPaymentRecyclerView()
        setupHelpRecyclerView()
        setupPromoOffersRecyclerView()
    }

    private fun observeViewModel() {
        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.textViewUserName.text = getString(R.string.dashboard_welcome_message, user.name)
            updateBalanceCard(user.balance)
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Met à jour la carte de solde avec la valeur fournie.
     * Formate le nombre pour un affichage localisé et propre (ex: "14,750").
     * @param balance Le solde à afficher.
     */
    private fun updateBalanceCard(balance: Double) {
        val balanceCardBinding = binding.viewBalanceCardV2
        val numberFormat = NumberFormat.getNumberInstance(Locale.FRANCE).apply {
            minimumFractionDigits = 3
            maximumFractionDigits = 3
        }
        val formattedBalance = numberFormat.format(balance)
        balanceCardBinding.textViewBalanceAmount.text = formattedBalance
    }

    /**
     * Configure les listeners de clics statiques de l'interface.
     */
    private fun setupClickListeners() {
        binding.viewBalanceCardV2.fabRecharge.setOnClickListener {
            Toast.makeText(requireContext(), "Bouton Recharge rapide cliqué !", Toast.LENGTH_SHORT).show()
            // Plus tard : findNavController().navigate(R.id.action_to_recharge)
        }

        // MODIFICATION AJOUTÉE : Ajout du listener pour le bouton profil.
        // L'ID 'button_profile' vient de votre layout fragment_dashboard.xml
        binding.buttonProfile.setOnClickListener {
            // Utilise l'action que nous avons définie dans le graphe de navigation.
            findNavController().navigate(R.id.action_dashboardFragment_to_profileFragment)
        }
    }


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

    private fun setupPromotionsRecyclerView() {
        val promotionItems = listOf(
            PromotionItem.FeaturedOffer(
                title = "Double",
                subtitle = "validité",
                imageResId = R.drawable.promo_image_double_validite,
                destinationId = R.id.offersFragment
            ),
            PromotionItem.StandardAction(
                DashboardAction(R.string.action_promos, R.drawable.ic_promos, R.id.offersFragment)
            ),
            PromotionItem.StandardAction(
                DashboardAction(R.string.action_wheel_of_chance, R.drawable.ic_wheel_of_chance, R.id.offersFragment)
            )
        )
        val promotionsAdapter = PromotionsAdapter(promotionItems) { destinationId ->
            destinationId?.let {
                findNavController().navigate(it)
            }
        }
        binding.recyclerViewPromotions.adapter = promotionsAdapter
    }

    private fun setupPaymentRecyclerView() {
        val paymentActions = listOf(
            DashboardAction(R.string.action_transfer_internet, R.drawable.ic_transfer_internet, R.id.invoicesFragment),
            DashboardAction(R.string.action_transfer_credit, R.drawable.ic_transfer_credit, R.id.invoicesFragment),
            DashboardAction(R.string.action_pay_invoice, R.drawable.ic_pay_invoice, R.id.invoicesFragment),
            DashboardAction(R.string.action_roaming, R.drawable.ic_roaming, R.id.offersFragment)
        )
        val paymentAdapter = DashboardActionAdapter(paymentActions) { destinationId ->
            findNavController().navigate(destinationId)
        }
        binding.recyclerViewPayment.adapter = paymentAdapter
    }

    private fun setupHelpRecyclerView() {
        val helpActions = listOf(
            DashboardAction(R.string.action_my_claims, R.drawable.ic_my_claims, R.id.contactSupportFragment),
            DashboardAction(R.string.action_chat_expert, R.drawable.ic_chat_expert, R.id.contactSupportFragment),
            DashboardAction(R.string.action_stores, R.drawable.ic_stores, R.id.contactSupportFragment),
            DashboardAction(R.string.action_customer_service, R.drawable.ic_customer_service, R.id.contactSupportFragment)
        )
        val helpAdapter = DashboardActionAdapter(helpActions) { destinationId ->
            findNavController().navigate(destinationId)
        }
        binding.recyclerViewHelp.adapter = helpAdapter
    }

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
            )
        )
        val promoOfferAdapter = PromoOfferAdapter(promoOffers) { destinationId ->
            destinationId?.let { findNavController().navigate(it) }
        }
        binding.recyclerViewPromoOffers.adapter = promoOfferAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}