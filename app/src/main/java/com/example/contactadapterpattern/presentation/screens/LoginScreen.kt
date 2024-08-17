package com.example.contactadapterpattern.presentation.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.contactadapterpattern.R
import com.example.contactadapterpattern.databinding.ScreenLoginBinding
import com.example.contactadapterpattern.presentation.viewmodel.LoginViewModel
import com.example.contactadapterpattern.presentation.viewmodel.impl.LoginViewModelImpl
import com.example.contactadapterpattern.utils.myApply
import com.example.contactadapterpattern.utils.text2
import com.example.contactadapterpattern.utils.toPhoneHandMade
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.widget.textChanges

@AndroidEntryPoint
class LoginScreen : Fragment(R.layout.screen_login) {
    private val binding by viewBinding(ScreenLoginBinding::bind)
    private val viewModel: LoginViewModel by viewModels<LoginViewModelImpl>()

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.myApply {
        initView()
        flowInit()
    }

    private fun flowInit() = binding.myApply {
        viewModel.progressGone
            .onEach { progressBarState(state = it) }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)

        viewModel.errorMessage
            .onEach { tilPhone.error = it }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)

        combine(
            editPhone.textChanges().map { it.length == 12 },
            editPassword.textChanges().map { it.length == 8 },
            transform = { phone, password -> phone && password }
        ).onEach { btnSubmit.isEnabled = it }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
    }

    private fun initView() = binding.myApply {
        btnSubmit.setOnClickListener {
            viewModel.onClickSubmit(
                phone = editPhone.toPhoneHandMade(),
                password = editPassword.text2()
            )
        }
        btnRegister.setOnClickListener {
            viewModel.onClickRegister()
        }
    }

    private fun progressBarState(state: Boolean) {
        binding.progress.isGone = state
        binding.btnSubmit.isClickable = state
        binding.btnSubmit.text = if (state) "Submit" else ""
    }
}