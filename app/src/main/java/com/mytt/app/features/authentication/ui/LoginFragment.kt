// Fichier : app/src/main/java/com/mytt/app/features/authentication/ui/LoginFragment.kt

package com.mytt.app.features.authentication.ui

import android.content.Intent
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
import com.mytt.app.databinding.FragmentLoginBinding
import com.mytt.app.features.admin.AdminActivity
import com.mytt.app.features.client.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString()
            viewModel.onLoginClicked(email, password)
        }

        // NOUVEAU : Ajout du listener pour la navigation vers l'écran d'enregistrement.
        binding.textViewCreateAccount.setOnClickListener {
            // Utilise l'action définie dans le auth_nav_graph.xml
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
    }

    private fun observeViewModel() {
        viewModel.errorEvent.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            handleLoadingState(isLoading)
        }

        viewModel.navigateToClientAreaEvent.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                navigateToMainActivity()
                viewModel.onClientNavigationDone()
            }
        }

        viewModel.navigateToAdminAreaEvent.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                navigateToAdminActivity()
                viewModel.onAdminNavigationDone()
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(activity, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    private fun navigateToAdminActivity() {
        val intent = Intent(activity, AdminActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    private fun handleLoadingState(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.textFieldEmail.isEnabled = !isLoading
        binding.textFieldPassword.isEnabled = !isLoading
        binding.buttonLogin.isEnabled = !isLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}