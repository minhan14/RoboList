package com.chicohan.samplelistapp.ui.authentication

import android.content.Intent
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.chicohan.samplelistapp.R
import com.chicohan.samplelistapp.data.entity.User
import com.chicohan.samplelistapp.databinding.FragmentLoginBinding
import com.chicohan.samplelistapp.helper.collectFlowWithLifeCycleAtStateStart
import com.chicohan.samplelistapp.helper.toast
import com.chicohan.samplelistapp.helper.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    companion object {
        const val LOGIN_SUCCESSFUL: String = "LOGIN_SUCCESSFUL"
    }
    private lateinit var binding: FragmentLoginBinding

    @Inject
    lateinit var glide: RequestManager

    private val authenticationViewModel by viewModels<AuthenticationViewModel>()

    private lateinit var savedStateHandle: SavedStateHandle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        setUpViews()
        collectFlowWithLifeCycleAtStateStart(authenticationViewModel.loginUserState) {
            handleLoginUiState(it)
        }
    }

    private fun handleLoginUiState(state: AuthenticationUiState) = with(state) {
        with(binding) {
            loading.getContentIfNotHandled()?.let {
                progressBarLogin.visible(it)
            }
            errorMessage.getContentIfNotHandled()?.let {
                requireContext().toast(it)
            }
            isSuccess.getContentIfNotHandled()?.let { user ->
                savedStateHandle[LOGIN_SUCCESSFUL] = true
                Log.d("LoginFragment", "LoginUiState>>${user}")
                val intent = requireActivity().intent
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                requireActivity().finish()
                startActivity(intent)
            }
        }
    }

    private fun setUpViews() = with(binding) {
        /**
         * kill the back stacks
         * previously using save state handle with homeFragment live data
         * move to handle with sharedPrefs
         */
        savedStateHandle = findNavController().previousBackStackEntry!!.savedStateHandle
        savedStateHandle[LOGIN_SUCCESSFUL] = false
        requireActivity().onBackPressedDispatcher.addCallback(this@LoginFragment) {
            activity?.finish()
        }
        loadAnimatedVectorDrawable(imageViewRobot, R.drawable.avd_robot_black)
        buttonLogin.setOnClickListener {
            val name = editTextUserName.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            authenticationViewModel.authenticateUser(name, password)
        }
        textViewGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_login_to_registerFragment)
        }

    }

    private fun loadAnimatedVectorDrawable(imageView: ImageView, avdResId: Int) {
        glide.load(avdResId)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    imageView.setImageDrawable(resource)
                    if (resource is Animatable) {
                        resource.start()
                    }
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                    imageView.setImageDrawable(placeholder)
                }
            })
    }
}