package com.chicohan.samplelistapp.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.chicohan.samplelistapp.R
import com.chicohan.samplelistapp.data.entity.SampleListItem
import com.chicohan.samplelistapp.data.entity.User
import com.chicohan.samplelistapp.databinding.FragmentHomeBinding
import com.chicohan.samplelistapp.helper.MockItem
import com.chicohan.samplelistapp.helper.collectFlowWithLifeCycleAtStateStart
import com.chicohan.samplelistapp.ui.adapter.ArchLayoutManager
import com.chicohan.samplelistapp.ui.adapter.MyListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.parcelize.Parcelize

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    val list = MockItem.sampleList.toMutableList()

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

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
//        myListAdapter.submitList(MockItem.sampleList)
        collectFlowWithLifeCycleAtStateStart(homeViewModel.user) { user: User? ->
            if (user == null) findNavController().navigate(R.id.action_navigation_home_to_navigation_login)
        }
        collectFlowWithLifeCycleAtStateStart(homeViewModel.toDoItems) {
            myListAdapter.submitList(it)
        }
    }

    private fun setUpViews() = with(binding) {
        recyclerViewMyList.apply {
            layoutManager = ArchLayoutManager(requireContext())
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
            findNavController().navigate(R.id.action_navigation_home_to_navigation_profile)
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

