package com.chicohan.samplelistapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chicohan.samplelistapp.R
import com.chicohan.samplelistapp.data.entity.User
import com.chicohan.samplelistapp.databinding.FragmentHomeBinding
import com.chicohan.samplelistapp.helper.UserPreferences
import com.chicohan.samplelistapp.helper.collectFlowWithLifeCycleAtStateStart
import com.chicohan.samplelistapp.ui.authentication.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val authenticationViewModel by viewModels<AuthenticationViewModel>()

    private val homeViewModel by activityViewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlowWithLifeCycleAtStateStart(homeViewModel.user) { user: User? ->
            user?.let {
                binding.textHome.text = user.name
            } ?: run {
                findNavController().navigate(R.id.action_navigation_home_to_navigation_login)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        binding.animationRobotView.setOnClickListener {
            homeViewModel.logoutUser()
//            findNavController().navigate(R.id.action_navigation_home_to_navigation_login)
        }
    }
}