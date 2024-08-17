package com.example.contactadapterpattern.presentation.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.contactadapterpattern.R
import com.example.contactadapterpattern.databinding.ScreenVerifyBinding
import com.example.contactadapterpattern.presentation.viewmodel.VerifyViewModel
import com.example.contactadapterpattern.presentation.viewmodel.impl.VerifyViewModelImpl
import com.example.contactadapterpattern.utils.myApply
import com.example.contactadapterpattern.utils.text2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.widget.textChanges

@AndroidEntryPoint
class VerifyCodeScreen : Fragment(R.layout.screen_verify) {
    private val viewModel: VerifyViewModel by viewModels<VerifyViewModelImpl>()
    private val binding by viewBinding(ScreenVerifyBinding::bind)
    private val navArgs: VerifyCodeScreenArgs by navArgs()

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initFlow()
    }

    private fun initFlow() = binding.myApply {
        viewModel.errorMessage
            .onEach {
                tilCode.isErrorEnabled = true
                tilCode.error = it
            }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
        viewModel.progress
            .onEach { progressState(it) }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)

        editCode
            .textChanges()
            .onEach { btnSubmit.isEnabled = it.length == 6 }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
    }

    private fun initView() = binding.myApply {
        editCode.addTextChangedListener { clearError() }
        btnSubmit.setOnClickListener {
            viewModel.onClickSubmit(
                phone = navArgs.phone,
                code = editCode.text2()
            )
        }
    }

    private fun progressState(state : Boolean) {
        binding.progress.isGone = state
        binding.btnSubmit.isClickable = state
        binding.btnSubmit.text = if (state) "Submit" else ""
    }
    private fun clearError() = binding.myApply {
        if(!tilCode.isErrorEnabled)
            tilCode.isErrorEnabled = false
    }
}