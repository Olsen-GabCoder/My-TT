// CORRIGÉ : Le package correspond maintenant à l'arborescence validée.
package com.mytt.app.features.client.billing.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mytt.app.databinding.FragmentInvoicesBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment qui affiche la liste des factures et permet leur paiement.
 * CORRIGÉ : Utilise notre architecture standard (Hilt, ViewBinding) et non
 * le template par défaut d'Android Studio.
 */
@AndroidEntryPoint
class InvoicesFragment : Fragment() {

    private var _binding: FragmentInvoicesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInvoicesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}