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
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.contactadapterpattern.R
import com.example.contactadapterpattern.databinding.ScreenAddContactBinding
import com.example.contactadapterpattern.presentation.viewmodel.AddContactViewModel
import com.example.contactadapterpattern.presentation.viewmodel.impl.AddContactViewModelImpl
import com.example.contactadapterpattern.utils.myApply
import com.example.contactadapterpattern.utils.text2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AddContactScreen : Fragment(R.layout.screen_add_contact) {
    private val viewModel: AddContactViewModel by viewModels<AddContactViewModelImpl>()
    private val binding by viewBinding(ScreenAddContactBinding::bind)

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.myApply {
        initView()
        initFlow()
    }

    private fun initFlow() = binding.myApply {
        viewModel.errorMessage
            .onEach {
                tilPhone.isErrorEnabled = true
                tilPhone.error = it
            }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)

        viewModel.progressState
            .onEach { progressState(it) }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
    }

    private fun initView() = binding.myApply {

        etFirstName.addTextChangedListener { checkEnableBtn() }

        etLastName.addTextChangedListener { checkEnableBtn() }

        etPhone.addTextChangedListener { checkEnableBtn() }

        btnBack.setOnClickListener { viewModel.onClickBack() }

        btnSave.setOnClickListener {
            viewModel.onClickSave(
                firstName = etFirstName.text2(),
                lastName = etLastName.text2(),
                phone = "+998" + etPhone.text2().replace(" ", "")
            )
        }
    }
    private fun progressState(state : Boolean) = binding.myApply  {
        progress.isGone = state
        btnSave.isClickable = state
        btnSave.text = if (state) "Save" else ""
    }


    private fun checkEnableBtn() {
        binding.btnSave.isEnabled =
            binding.etFirstName.text2().length > 3 &&
                    binding.etLastName.text2().length > 3 &&
                    binding.etPhone.text2().length == 12
    }
}