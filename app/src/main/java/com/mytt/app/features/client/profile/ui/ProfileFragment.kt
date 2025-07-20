package com.mytt.app.features.client.profile.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mytt.app.R
import com.mytt.app.databinding.FragmentProfileBinding
import com.mytt.app.databinding.ItemProfileActionBinding
import com.mytt.app.features.authentication.ui.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupActionItems()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupToolbar() {
        binding.toolbarProfile.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupActionItems() {
        setupAction(binding.actionInfo, "Mes informations", R.drawable.ic_profile) {
            Toast.makeText(requireContext(), "Mes informations cliqué", Toast.LENGTH_SHORT).show()
        }
        setupAction(binding.actionPurchases, "Mes achats", R.drawable.ic_shopping_bag) {
            Toast.makeText(requireContext(), "Mes achats cliqué", Toast.LENGTH_SHORT).show()
        }
        setupAction(binding.actionFavorites, "Mes favoris", R.drawable.ic_favorite) {
            Toast.makeText(requireContext(), "Mes favoris cliqué", Toast.LENGTH_SHORT).show()
        }
        setupAction(binding.actionSettings, "Paramètres", R.drawable.ic_settings) {
            Toast.makeText(requireContext(), "Paramètres cliqué", Toast.LENGTH_SHORT).show()
        }
        setupAction(binding.actionRateApp, "Noter l'application", R.drawable.ic_star) {
            Toast.makeText(requireContext(), "Noter l'application cliqué", Toast.LENGTH_SHORT).show()
        }
        setupAction(binding.actionSupport, "Assistance", R.drawable.ic_help_outline) {
            Toast.makeText(requireContext(), "Assistance cliqué", Toast.LENGTH_SHORT).show()
        }
        setupAction(binding.actionShare, "Partager l'application", R.drawable.ic_share) {
            shareApp()
        }
    }

    private fun setupAction(itemBinding: ItemProfileActionBinding, title: String, iconRes: Int, onClick: () -> Unit) {
        itemBinding.title.text = title
        itemBinding.icon.setImageResource(iconRes)
        itemBinding.root.setOnClickListener { onClick() }
    }

    private fun setupClickListeners() {
        binding.buttonLogout.setOnClickListener {
            viewModel.onLogoutClicked()
        }
    }

    private fun observeViewModel() {
        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.textViewProfileName.text = user.name
            binding.textViewProfilePhone.text = user.phoneNumber
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
        }

        viewModel.logoutEvent.observe(viewLifecycleOwner) {
            navigateToAuthScreen()
        }
    }

    private fun navigateToAuthScreen() {
        val intent = Intent(requireActivity(), AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        requireActivity().finish()
    }

    private fun shareApp() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Découvrez l'application MyTT ! [Lien vers le Play Store]")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}