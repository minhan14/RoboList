package com.chicohan.samplelistapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor():ViewModel() {
    private val _isSplashVisible = MutableLiveData(true)
    val isSplashVisible: LiveData<Boolean> get() = _isSplashVisible

    fun setSplashVisibility(isVisible: Boolean) {
        _isSplashVisible.value = isVisible
    }
}