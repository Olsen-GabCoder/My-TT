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
import com.mytt.app.databinding.FragmentPhoneEntryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhoneEntryFragment : Fragment() {

    private var _binding: FragmentPhoneEntryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PhoneEntryViewModel by viewModels()
    private var phoneNumber: String = "" // Stocke le numéro national

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhoneEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        observeUiState()
    }

    private fun setupClickListeners() {
        binding.textViewLoginWithPassword.setOnClickListener {
            findNavController().navigate(R.id.action_phoneEntryFragment_to_loginFragment)
        }

        binding.buttonSendOtp.setOnClickListener {
            phoneNumber = binding.editTextPhone.text.toString().trim()

            if (phoneNumber.length != 8 || !phoneNumber.matches(Regex("[0-9]+"))) {
                Toast.makeText(requireContext(), "Veuillez entrer un numéro de téléphone valide (8 chiffres).", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            binding.buttonSendOtp.isEnabled = false
            // MODIFIÉ : On envoie maintenant le numéro national (8 chiffres) au ViewModel.
            viewModel.sendOtp(phoneNumber, requireActivity() as AuthActivity)
        }
    }

    private fun observeUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.progressBar.isVisible = state is PhoneAuthUiState.Loading

            when (state) {
                is PhoneAuthUiState.CodeSent -> {
                    Toast.makeText(requireContext(), "Code envoyé avec succès !", Toast.LENGTH_SHORT).show()

                    val fullPhoneNumber = "+216$phoneNumber"
                    val action = PhoneEntryFragmentDirections.actionPhoneEntryFragmentToOtpEntryFragment(
                        verificationId = state.verificationId,
                        phoneNumber = fullPhoneNumber
                    )
                    findNavController().navigate(action)

                    viewModel.resetState()
                }
                is PhoneAuthUiState.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    binding.buttonSendOtp.isEnabled = true
                    viewModel.resetState()
                }
                is PhoneAuthUiState.Idle -> {
                    binding.buttonSendOtp.isEnabled = true
                }
                is PhoneAuthUiState.Loading -> {
                    binding.buttonSendOtp.isEnabled = false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}