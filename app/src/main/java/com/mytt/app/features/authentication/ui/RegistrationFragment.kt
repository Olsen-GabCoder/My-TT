// Fichier : app/src/main/java/com/mytt/app/features/authentication/ui/RegistrationFragment.kt

package com.mytt.app.features.authentication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mytt.app.R
import com.mytt.app.databinding.FragmentRegistrationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegistrationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeUiState()
    }

    private fun setupClickListeners() {
        binding.textViewLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }

        binding.buttonRegister.setOnClickListener {
            val name = binding.editTextFullName.text.toString().trim()
            val phone = binding.editTextPhone.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString()

            viewModel.onRegisterClicked(name, phone, email, password)
        }
    }

    private fun observeUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            handleLoadingState(state is RegistrationUiState.Loading)

            when (state) {
                is RegistrationUiState.RegistrationSuccess -> {
                    Toast.makeText(requireContext(), "Compte créé avec succès ! Vous pouvez maintenant vous connecter.", Toast.LENGTH_LONG).show()

                    // MODIFICATION : Rediriger vers l'écran de connexion principal (PhoneEntryFragment)
                    navigateToLoginScreen()
                }
                is RegistrationUiState.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    viewModel.resetState()
                }
                else -> {
                    // Ne rien faire pour les états Idle ou Loading car déjà gérés
                }
            }
        }
    }

    private fun handleLoadingState(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.textFieldFullName.isEnabled = !isLoading
        binding.textFieldPhone.isEnabled = !isLoading
        binding.textFieldEmail.isEnabled = !isLoading
        binding.textFieldPassword.isEnabled = !isLoading
        binding.buttonRegister.isEnabled = !isLoading
        binding.textViewLogin.isEnabled = !isLoading
    }

    /**
     * Navigue vers l'écran de connexion principal du graphe d'authentification.
     * On nettoie la pile de navigation jusqu'à la destination de départ pour que
     * l'utilisateur ne puisse pas revenir à l'écran d'enregistrement.
     */
    private fun navigateToLoginScreen() {
        findNavController().popBackStack(R.id.phoneEntryFragment, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}