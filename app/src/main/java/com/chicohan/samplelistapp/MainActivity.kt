package com.chicohan.samplelistapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.chicohan.samplelistapp.databinding.ActivityMainBinding
import com.chicohan.samplelistapp.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var keepSplashOnScreen = true
    private val delay = 1500L
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * delay splash animation for specific time
         * commented due to having bugs on config changes
         */
         installSplashScreen()
//        splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }
//        Handler(Looper.getMainLooper()).postDelayed({ keepSplashOnScreen = false }, delay)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}