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
import com.example.contactadapterpattern.databinding.ScreenEditContactBinding
import com.example.contactadapterpattern.presentation.viewmodel.EditContactViewModel
import com.example.contactadapterpattern.presentation.viewmodel.impl.EditContactViewModelImpl
import com.example.contactadapterpattern.utils.myApply
import com.example.contactadapterpattern.utils.text2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class EditContactScreen : Fragment(R.layout.screen_edit_contact) {
    private val binding by viewBinding(ScreenEditContactBinding::bind)
    private val viewModel: EditContactViewModel by viewModels<EditContactViewModelImpl>()
    private val navArgs: EditContactScreenArgs by navArgs()

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.myApply {
        initView()
        initFlow()
    }

    private fun initFlow() = binding.myApply {

        viewModel.progressState
            .onEach { progressState(it) }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)

        viewModel.errorMessage
            .onEach {
                tilPhone.isErrorEnabled = true
                tilPhone.error = it
            }.flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
    }

    private fun initView() = binding.myApply {
        etFirstName.setText(navArgs.contact.firstName)
        etLastName.setText(navArgs.contact.lastName)
        etPhone.setText(navArgs.contact.phone.substring(4))
        etPhone.addTextChangedListener { checkEnableBtn() }
        etLastName.addTextChangedListener { checkEnableBtn() }
        etFirstName.addTextChangedListener { checkEnableBtn() }
        btnSave.setOnClickListener {
            viewModel.onClickSave(
                contactUIData = navArgs.contact,
                firstName = etFirstName.text2(),
                lastName = etLastName.text2(),
                phone = "+998" + etPhone.text2().replace(" ", "")
            )
        }
        btnBack.setOnClickListener { viewModel.onClickBack() }
    }

    private fun progressState(state: Boolean) {
        binding.progress.isGone = state
        binding.btnSave.isClickable = state
        binding.btnSave.text = if (state) "Save" else ""
    }

    private fun checkEnableBtn() {
        binding.btnSave.isEnabled =
            binding.etFirstName.text2().length > 3 &&
                    binding.etLastName.text2().length > 3 &&
                    binding.etPhone.text2().length == 12
                    && (binding.etFirstName.text2() != navArgs.contact.firstName ||
                    binding.etLastName.text2() != navArgs.contact.lastName ||
                    binding.etPhone.text2().replace(" ", "") != navArgs.contact.phone.substring(4))
    }
}