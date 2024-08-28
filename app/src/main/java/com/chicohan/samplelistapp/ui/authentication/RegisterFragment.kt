package com.chicohan.samplelistapp.ui.authentication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.chicohan.samplelistapp.R
import com.chicohan.samplelistapp.databinding.FragmentRegisterBinding
import com.chicohan.samplelistapp.helper.collectFlowWithLifeCycleAtStateStart
import com.chicohan.samplelistapp.helper.toast
import com.chicohan.samplelistapp.helper.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding

    private val authenticationViewModel: AuthenticationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)
        setUpViews()
        collectFlowWithLifeCycleAtStateStart(authenticationViewModel.registerUserState) {
            handleRegisterUiState(it)
        }
    }

    private fun handleRegisterUiState(registerUiState: AuthenticationUiState) =
        with(registerUiState) {
            with(binding) {
                loading.getContentIfNotHandled()?.let {
                    progressBarRegister.visible(it)
                }
                errorMessage.getContentIfNotHandled()?.let {
                    requireContext().toast(it)
                }
                isSuccess.getContentIfNotHandled()?.let { user ->
                    Log.d("RegisterFragment", "RegisterUiState>>${user}")
                    findNavController().popBackStack()
                }
            }
        }

    private fun setUpViews() = with(binding) {

        buttonRegister.setOnClickListener {
            val name = editTextUserName.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val confirmPassword = editTextConfirmPassword.text.toString().trim()

            /**
            - call the register function
             */
            lifecycleScope.launch {
                authenticationViewModel.registerUser(name, password, confirmPassword)
            }


        }
    }

}