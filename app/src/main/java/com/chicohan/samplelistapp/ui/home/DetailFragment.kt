package com.chicohan.samplelistapp.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.chicohan.samplelistapp.R
import com.chicohan.samplelistapp.data.entity.SampleListItem
import com.chicohan.samplelistapp.databinding.FragmentDetailBinding
import com.chicohan.samplelistapp.helper.createGenericAlertDialog
import com.chicohan.samplelistapp.helper.toast
import com.chicohan.samplelistapp.helper.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private lateinit var binding: FragmentDetailBinding

    private val navArgs: DetailFragmentArgs by navArgs()

    @Inject
    lateinit var glide: RequestManager

    private val homeViewModel by activityViewModels<HomeViewModel>()

    private var imageUri: String? = null

    //gallery launcher
    private val startGalleryResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val photoUri = result.data?.data
            imageUri = photoUri.toString()
            imageUri?.let {
                glide.load(it).into(binding.ivMyPhoto)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailBinding.bind(view)
        val operations = navArgs.operationArgs
        operations?.let { op ->
            setUpViews(op.dest)
            when (op.dest) {
                TaskOperations.ADD_NEW_TASK -> {}
                TaskOperations.EDIT_TASK -> {
                    val item = navArgs.MyToDoItemArgs ?: return@let
                    setUpEditView(item)
                }
                TaskOperations.DELETE_TASK -> Unit
            }
        } ?: run {
            setUpViews(TaskOperations.ADD_NEW_TASK)
        }

    }

    private fun setUpEditView(item: SampleListItem) = with(binding) {
        "Edit Your Task".also { txtHeader.text = it }
        txtName.setText(item.name)
        imageUri = item.imageUri
        glide.load(item.imageUri)
            .into(ivMyPhoto)
        editTextTextMultiLineDescription.setText(item.description)
        "Edit Task".also { buttonConfirm.text = it }
    }

    private fun setUpViews(ops: TaskOperations) = with(binding) {
        btnDelete.visible(ops == TaskOperations.EDIT_TASK)
        buttonCancel.setOnClickListener {
            findNavController().popBackStack()
        }
        ivMyPhoto.setOnClickListener {
            openGallery()
        }
        btnDelete.setOnClickListener {
            val itemId = navArgs.MyToDoItemArgs?.id ?: return@setOnClickListener
            lifecycleScope.launch {
                requireContext().createGenericAlertDialog(
                    "Delete Task",
                    "Are you sure you want to Delete this Task?",
                    "Yes",
                    "No"
                ) {
                    if (it){
                        homeViewModel.deleteTask(itemId)
                        findNavController().popBackStack()
                    }
                }

            }
        }
        buttonConfirm.setOnClickListener {
            lifecycleScope.launch {
                val userId = homeViewModel.user.first()?.id ?: return@launch
                val name = txtName.text.toString()
                val description = editTextTextMultiLineDescription.text.toString()
                if (name.isEmpty() || description.isEmpty() || imageUri.isNullOrEmpty()) {
                    requireContext().toast("Fields cannot be empty")
                    return@launch
                }
                val task = SampleListItem(
                    userId = userId,
                    name = name,
                    imageUri = imageUri,
                    description = description
                )
                when (ops) {
                    TaskOperations.ADD_NEW_TASK -> homeViewModel.addTask(task)
                    TaskOperations.EDIT_TASK -> {
                        val itemId = navArgs.MyToDoItemArgs?.id ?: return@launch
                        homeViewModel.editTask(itemId = itemId, task)
                    }
                    TaskOperations.DELETE_TASK -> Unit
                }
                findNavController().popBackStack()
            }

        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startGalleryResult.launch(intent)
    }
}