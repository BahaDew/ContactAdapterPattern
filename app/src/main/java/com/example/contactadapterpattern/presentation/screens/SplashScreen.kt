package com.example.contactadapterpattern.presentation.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.contactadapterpattern.R
import com.example.contactadapterpattern.databinding.ScreenSplashBinding
import com.example.contactadapterpattern.presentation.viewmodel.SplashViewModel
import com.example.contactadapterpattern.presentation.viewmodel.impl.SplashViewModelImpl
import com.example.contactadapterpattern.utils.myApply
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreen : Fragment(R.layout.screen_splash) {
    private val viewModel: SplashViewModel by viewModels<SplashViewModelImpl>()
    private val binding: ScreenSplashBinding by viewBinding(ScreenSplashBinding::bind)
    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.myApply {
        viewModel.openSplash
            .onEach {}
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
    }
}