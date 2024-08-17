package com.example.contactadapterpattern.presentation.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.contactadapterpattern.R
import com.example.contactadapterpattern.databinding.ScreenContactBinding
import com.example.contactadapterpattern.presentation.adapter.ContactListAdapter
import com.example.contactadapterpattern.presentation.viewmodel.ContactViewModel
import com.example.contactadapterpattern.presentation.viewmodel.impl.ContactViewModelImpl
import com.example.contactadapterpattern.utils.myApply
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

@AndroidEntryPoint
class ContactScreen : Fragment(R.layout.screen_contact) {
    private val binding by viewBinding(ScreenContactBinding::bind)
    private val viewModel: ContactViewModel by viewModels<ContactViewModelImpl>()
    private val adapter by lazy(LazyThreadSafetyMode.NONE) { ContactListAdapter() }
    private lateinit var popupMenu: PopupMenu

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.myApply {
        initView()
        initFlow()
    }

    private fun initFlow() = binding.myApply {

        viewModel.placeHolderState
            .onEach { imgPlaceholder.isGone = it }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)

        viewModel.refreshingState
            .onEach { refreshL.isRefreshing = it }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)

        viewModel.allContact
            .onEach { adapter.submitList2(it) }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)

        viewModel.netWorkConnectionState
            .onEach { imgNoConnection.isGone = it }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)

        btnAddFloating.clicks()
            .onEach { viewModel.onClickAdd() }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
    }

    private fun initView() = binding.myApply {
        rvList.adapter = adapter
        rvList.layoutManager = LinearLayoutManager(requireContext())

        adapter.setOnClickEdit { viewModel.onClickEdit(it) }
        adapter.onClickDelete { viewModel.onClickDelete(it) }

        refreshL.setOnRefreshListener {
            lifecycleScope.launch {
                delay(1000)
                refreshL.isRefreshing = false
            }
        }

        popupMenu = PopupMenu(requireContext(), binding.btnOption)
        popupMenu.menuInflater.inflate(R.menu.pop_up_logout, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            if (it.itemId == R.id.logout) {
//                viewModel.onClickLogOut()
            }
            true
        }
        btnOption.setOnClickListener {
            popupMenu.show()
        }
    }
}