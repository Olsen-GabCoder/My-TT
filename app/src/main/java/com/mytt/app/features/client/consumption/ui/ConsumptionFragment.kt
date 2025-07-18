package com.mytt.app.features.client.consumption.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mytt.app.databinding.FragmentConsumptionBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment qui affiche le détail de la consommation de l'utilisateur.
 */
@AndroidEntryPoint
class ConsumptionFragment : Fragment() {

    private var _binding: FragmentConsumptionBinding? = null
    private val binding get() = _binding!!

    // Connexion au ViewModel.
    private val viewModel: ConsumptionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConsumptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // NOUVEAU : Mise en place des observateurs.
        observeViewModel()
    }

    /**
     * NOUVEAU : Fonction qui observe les LiveData du ViewModel et met à jour l'UI.
     */
    private fun observeViewModel() {
        viewModel.consumptionState.observe(viewLifecycleOwner) { state ->
            // Met à jour chaque TextView avec les données de l'objet State.
            binding.textViewBalanceValue.text = state.mainBalance
            binding.textViewCallBalanceValue.text = state.callBalance
            binding.textViewInternetBalanceValue.text = state.internetBalance
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}