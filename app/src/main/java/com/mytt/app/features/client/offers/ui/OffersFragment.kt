package com.mytt.app.features.client.offers.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mytt.app.databinding.FragmentOffersBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment qui affiche la liste des offres disponibles.
 */
@AndroidEntryPoint
class OffersFragment : Fragment() {

    private var _binding: FragmentOffersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OffersViewModel by viewModels()
    private lateinit var offersAdapter: OffersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOffersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        offersAdapter = OffersAdapter()
        binding.recyclerViewOffers.adapter = offersAdapter
    }

    /**
     * Observe les LiveData du ViewModel et met à jour l'UI.
     */
    private fun observeViewModel() {
        viewModel.offers.observe(viewLifecycleOwner) { offers ->
            offersAdapter.submitList(offers)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        // NOUVEAU : Observation de l'événement d'erreur.
        viewModel.errorEvent.observe(viewLifecycleOwner) { errorMessage ->
            // Affiche un message Toast avec le message d'erreur reçu.
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}