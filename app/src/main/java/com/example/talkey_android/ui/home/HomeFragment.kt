package com.example.talkey_android.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.talkey_android.data.domain.use_cases.chats.GetListChatsUseCase
import com.example.talkey_android.data.domain.use_cases.users.GetListProfilesUseCase
import com.example.talkey_android.databinding.FragmentHomeBinding
import com.example.talkey_android.ui.home.adapter.ContactsAdapter
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), ContactsAdapter.CellListener {

    private val args: HomeFragmentArgs by navArgs()
    private lateinit var mBinding: FragmentHomeBinding
    private lateinit var mAdapter: ContactsAdapter
    private val mViewHolder = HomeFragmentViewModel(GetListProfilesUseCase(), GetListChatsUseCase())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupAdapter()
        observeViewModel()
        mViewHolder.getUsersList(args.token)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            mViewHolder.users.collect {

            }
        }
    }

    private fun setupAdapter() {
        mAdapter = ContactsAdapter(requireContext(), this)
        val listManager = LinearLayoutManager(requireContext())

        with(mBinding) {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = listManager
            recyclerView.adapter = mAdapter
        }

    }

    override fun onContactClick(token: String) {
    }
}