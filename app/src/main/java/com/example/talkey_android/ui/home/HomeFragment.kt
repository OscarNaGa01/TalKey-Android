package com.example.talkey_android.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.talkey_android.R
import com.example.talkey_android.data.domain.use_cases.chats.CreateChatUseCase
import com.example.talkey_android.data.domain.use_cases.chats.GetListChatsUseCase
import com.example.talkey_android.data.domain.use_cases.messages.GetListMessageUseCase
import com.example.talkey_android.data.domain.use_cases.users.GetListProfilesUseCase
import com.example.talkey_android.databinding.FragmentHomeBinding
import com.example.talkey_android.ui.home.adapter.ContactsAdapter
import kotlinx.coroutines.launch

class HomeFragment
    : Fragment(),
    ContactsAdapter.CellListener,
    androidx.appcompat.widget.SearchView.OnQueryTextListener {


    enum class ListType {
        CONTACTS,
        CHATS
    }

    private val args: HomeFragmentArgs by navArgs()
    private var listType = ListType.CHATS
    private lateinit var mBinding: FragmentHomeBinding
    private lateinit var mAdapter: ContactsAdapter
    private val mViewModel = HomeFragmentViewModel(
        GetListProfilesUseCase(),
        GetListChatsUseCase(),
        GetListMessageUseCase(),
        CreateChatUseCase()
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        setupToolbar()
        return mBinding.root
    }

    private fun setupToolbar() {
        val editProfileIndex = 0
        val logoutIndex = 1
        mBinding.toolBar.inflateMenu(R.menu.menu_fragment_home)
        mBinding.icSearch.setOnClickListener {
            with(mBinding.searchView) {
                if (isVisible) {
                    visibility = View.GONE
                } else {
                    visibility = View.VISIBLE
                    isIconified = false
                    requestFocus()
                }
            }
        }
        mBinding.toolBar.menu.getItem(editProfileIndex).setOnMenuItemClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToProfileFragment(
                    args.id,
                    args.token,
                    false
                )
            )
            true
        }
        mBinding.toolBar.menu.getItem(logoutIndex).setOnMenuItemClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToLogin())
            true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mBinding.searchView.setOnQueryTextListener(this)
        mBinding.searchView.setOnCloseListener {
            mViewModel.removeFilters(listType)
            true
        }
        setupAdapter()
        observeViewModel()
        mViewModel.getChatsList(args.token, args.id)

        with(mBinding) {
            btnChats.setOnClickListener {
                vSelectedChats.visibility = View.VISIBLE
                vSelectedContacts.visibility = View.INVISIBLE
                mViewModel.getChatsList(args.token, args.id)
                listType = ListType.CHATS
            }

            btnContacts.setOnClickListener {
                vSelectedChats.visibility = View.INVISIBLE
                vSelectedContacts.visibility = View.VISIBLE
                mViewModel.getUsersList(args.token)
                listType = ListType.CONTACTS
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            mViewModel.idNewChat.collect { idNewChat ->
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeToChat(
                        args.token,
                        args.id,
                        idNewChat
                    )
                )
            }
        }
        lifecycleScope.launch {
            mViewModel.createNewChatError.collect {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launch {
            mViewModel.uiState.collect {
                when (it) {
                    is HomeFragmentUiState.Loading -> {
                        mBinding.progressBarr.visibility = View.VISIBLE
                    }

                    is HomeFragmentUiState.Success -> {
                        mBinding.progressBarr.visibility = View.GONE
                        mAdapter.refreshData(it.dataList)
                    }

                    is HomeFragmentUiState.Error -> {
                        Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setupAdapter() {
        mAdapter = ContactsAdapter(requireContext(), this, args.token, args.id)
        val listManager = LinearLayoutManager(requireContext())

        with(mBinding) {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = listManager
            recyclerView.adapter = mAdapter
        }
    }

    override fun onContactClick(idContact: String) {
        mViewModel.createChat(args.token, args.id, idContact)
    }

    override fun onChatClick(idChat: String) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeToChat(
                args.token,
                args.id,
                idChat
            )
        )
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()) {
            mViewModel.filterListByName(query, listType)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?) = true
}