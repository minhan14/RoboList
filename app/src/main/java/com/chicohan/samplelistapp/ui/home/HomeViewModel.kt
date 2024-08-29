package com.chicohan.samplelistapp.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chicohan.samplelistapp.data.entity.SampleListItem
import com.chicohan.samplelistapp.data.entity.User
import com.chicohan.samplelistapp.data.repository.ToDoListRepository
import com.chicohan.samplelistapp.domain.UseCases
import com.chicohan.samplelistapp.helper.UserPreferences
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.switchMap
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val toDoListRepository: ToDoListRepository
) : ViewModel() {


    var user = MutableStateFlow<User?>(null)
        private set

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    @OptIn(ExperimentalCoroutinesApi::class)
    val toDoItems: StateFlow<List<SampleListItem>> = user
        .flatMapLatest { user ->
            user?.let { toDoListRepository.getToDoItems(it.id) } ?: flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        user.update { UserPreferences.getUser(context) }
    }

    fun logoutUser() {
        user.update { null }
        UserPreferences.clearUser(context)
    }

    fun addTask(item: SampleListItem) = viewModelScope.launch {
        try {
            toDoListRepository.addToDoList(item)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun editTask(itemId: Int, task: SampleListItem) = viewModelScope.launch {
        try {
            toDoListRepository.updateTodoItems(itemId = itemId, task)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }


}