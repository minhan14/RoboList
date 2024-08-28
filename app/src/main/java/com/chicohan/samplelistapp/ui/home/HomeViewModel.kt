package com.chicohan.samplelistapp.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chicohan.samplelistapp.data.entity.User
import com.chicohan.samplelistapp.helper.UserPreferences
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    var user = MutableStateFlow<User?>(null)
        private set

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    init {
        user.update { UserPreferences.getUser(context) }
    }

    fun logoutUser() {
        user.update { null }
        UserPreferences.clearUser(context)
    }


}