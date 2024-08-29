package com.chicohan.samplelistapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chicohan.samplelistapp.data.entity.User
import com.chicohan.samplelistapp.databinding.FragmentProfileBinding
import com.chicohan.samplelistapp.helper.collectFlowWithLifeCycleAtStateStart
import com.chicohan.samplelistapp.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        collectFlowWithLifeCycleAtStateStart(viewModel.user) { user: User? ->
            user?.let {
                binding.txtName.text = user.name
            }
        }
    }

    private fun setUpViews() = with(binding) {
        backBtn.setOnClickListener { findNavController().popBackStack() }
        buttonLogout.setOnClickListener {
            viewModel.logoutUser()
            val intent = requireActivity().intent
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            requireActivity().finish()
            startActivity(intent)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}