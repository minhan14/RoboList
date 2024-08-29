package com.chicohan.samplelistapp.ui.home

import android.app.Activity
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chicohan.samplelistapp.R
import com.chicohan.samplelistapp.data.entity.SampleListItem
import com.chicohan.samplelistapp.data.entity.User
import com.chicohan.samplelistapp.databinding.FragmentHomeBinding
import com.chicohan.samplelistapp.helper.MockItem
import com.chicohan.samplelistapp.helper.UserPreferences
import com.chicohan.samplelistapp.helper.collectFlowWithLifeCycleAtStateStart
import com.chicohan.samplelistapp.ui.adapter.ArchLayoutManager
import com.chicohan.samplelistapp.ui.adapter.MyListAdapter
import com.chicohan.samplelistapp.ui.authentication.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.parcelize.Parcelize

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    val list = MockItem.sampleList.toMutableList()

    private val authenticationViewModel by viewModels<AuthenticationViewModel>()

    private val homeViewModel by activityViewModels<HomeViewModel>()

    private val myListAdapter by lazy {
        MyListAdapter { item: SampleListItem ->
            val action = HomeFragmentDirections.actionNavigationHomeToDetailFragment(
                item,
                Operations(TaskOperations.EDIT_TASK)
            )
            findNavController().navigate(action)
            print(item)
        }
    }


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

        setUpViews()
//        myListAdapter.submitList(MockItem.sampleList)
        collectFlowWithLifeCycleAtStateStart(homeViewModel.user) { user: User? ->
            user?.let {
                binding.textHome.text = user.name
            } ?: run {
                findNavController().navigate(R.id.action_navigation_home_to_navigation_login)
            }
        }
        collectFlowWithLifeCycleAtStateStart(homeViewModel.toDoItems) {
            myListAdapter.submitList(it)
        }
    }

    private fun setUpViews() = with(binding) {
        recyclerViewMyList.apply {
            layoutManager = ArchLayoutManager(requireContext())
//            layoutManager =
//                LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            adapter = myListAdapter
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
        }
        binding.btnAdd.setOnClickListener {
            val action = HomeFragmentDirections.actionNavigationHomeToDetailFragment(
                null,
                Operations(TaskOperations.ADD_NEW_TASK)
            )
            findNavController().navigate(action)
        }
    }
}

@Parcelize
data class Operations(
    val dest: TaskOperations
) : Parcelable

enum class TaskOperations {
    ADD_NEW_TASK, EDIT_TASK, DELETE_TASK
}

