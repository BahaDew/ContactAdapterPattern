package com.example.contactadapterpattern.presentation.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.contactadapterpattern.R
import com.example.contactadapterpattern.databinding.ScreenRegisterBinding
import com.example.contactadapterpattern.presentation.viewmodel.RegisterViewModel
import com.example.contactadapterpattern.presentation.viewmodel.impl.RegisterViewModelImpl
import com.example.contactadapterpattern.utils.myApply
import com.example.contactadapterpattern.utils.text2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.widget.textChanges

@AndroidEntryPoint
class RegisterScreen : Fragment(R.layout.screen_register) {
    private val viewModel: RegisterViewModel by viewModels<RegisterViewModelImpl>()
    private val binding by viewBinding(ScreenRegisterBinding::bind)

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initFlow()
    }

    private fun initFlow() = binding.myApply {
        viewModel.progress
            .onEach { progressState(it) }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)

        viewModel.errorMessage
            .onEach {
                tilPhone.isErrorEnabled = true
                tilPhone.error = it }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)

        combine(
            editFirstName.textChanges().map { it.length > 3 },
            editLastName.textChanges().map { it.length > 3 },
            editPassword.textChanges().map { it.length == 8 },
            editPhone.textChanges().map { it.length == 12 },
            transform = { firstName, lastName, password, phone -> firstName && lastName && password && phone }
        ).onEach { btnNext.isEnabled = it }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
    }

    private fun initView() = binding.myApply {
        editFirstName.addTextChangedListener { clearError() }
        editLastName.addTextChangedListener { clearError() }
        editPassword.addTextChangedListener { clearError() }
        editPhone.addTextChangedListener { clearError() }
        binding.btnNext.setOnClickListener {
            viewModel.onClickNext(
                firstName = editFirstName.text2(),
                lastName = editLastName.text2(),
                phone = "+998" + editPhone.text2().replace(" ", ""),
                password = editPassword.text2()
            )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun progressState(state: Boolean) = binding.myApply {
        progress.isGone = state
        btnNext.text = if (state) "Next" else ""
        binding.btnNext.isClickable = state
    }

    private fun clearError() = binding.myApply {
        if (!tilPhone.isErrorEnabled)
            tilPhone.isErrorEnabled = false
    }
}