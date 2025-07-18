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
    }

    private fun observeViewModel() {
        viewModel.errorEvent.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            handleLoadingState(isLoading)
        }

        // MODIFIÉ : Observation de l'événement de navigation client.
        viewModel.navigateToClientAreaEvent.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                navigateToMainActivity()
                viewModel.onClientNavigationDone()
            }
        }

        // NOUVEAU : Observation de l'événement de navigation admin.
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

    /**
     * NOUVEAU : Fonction qui gère la navigation vers l'activité admin.
     */
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