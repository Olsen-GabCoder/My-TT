// Fichier : app/src/main/java/com/mytt/app/features/authentication/ui/OtpEntryFragment.kt

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
import androidx.navigation.fragment.navArgs
import com.mytt.app.databinding.FragmentOtpEntryBinding
import com.mytt.app.features.client.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtpEntryFragment : Fragment() {

    private var _binding: FragmentOtpEntryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OtpEntryViewModel by viewModels()
    private val args: OtpEntryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtpEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeUiState()
        observeTimer()
    }

    private fun setupClickListeners() {
        binding.buttonVerify.setOnClickListener {
            val otpCode = binding.editTextOtp.text.toString().trim()
            viewModel.verifyOtp(args.verificationId, otpCode)
        }

        binding.textViewResendCode.setOnClickListener {
            // TODO: Implémenter la logique de renvoi complet. Pour l'instant, on redémarre le timer.
            viewModel.startTimer()
            Toast.makeText(requireContext(), "Nouveau code demandé...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.progressBar.isVisible = state is OtpAuthUiState.Loading

            when (state) {
                is OtpAuthUiState.VerificationSuccess -> {
                    Toast.makeText(requireContext(), "Connexion réussie ! Bienvenue ${state.user.name}", Toast.LENGTH_LONG).show()

                    val intent = Intent(requireActivity(), MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    requireActivity().finish()
                }
                is OtpAuthUiState.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    binding.buttonVerify.isEnabled = true
                    viewModel.resetState()
                }
                is OtpAuthUiState.Idle -> {
                    binding.buttonVerify.isEnabled = true
                }
                is OtpAuthUiState.Loading -> {
                    binding.buttonVerify.isEnabled = false
                }
            }
        }
    }

    private fun observeTimer() {
        viewModel.timerText.observe(viewLifecycleOwner) { timerValue ->
            binding.textViewTimer.text = timerValue
        }

        viewModel.isResendEnabled.observe(viewLifecycleOwner) { isEnabled ->
            binding.textViewTimer.isVisible = !isEnabled
            binding.textViewResendCode.isVisible = isEnabled
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}