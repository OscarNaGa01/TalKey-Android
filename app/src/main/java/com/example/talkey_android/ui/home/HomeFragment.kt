package com.example.talkey_android.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.talkey_android.data.domain.use_cases.chats.GetListChatsUseCase
import com.example.talkey_android.data.domain.use_cases.users.GetListProfilesUseCase
import com.example.talkey_android.databinding.FragmentHomeBinding
import com.example.talkey_android.ui.home.adapter.ContactsAdapter
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), ContactsAdapter.CellListener {


    /*{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjI0MSIsImlhdCI6MTcxMDQ5NzQ4NSwiZXhwIjoxNzEzMDg5NDg1fQ.YVwmg-Cn8X1oWDkNkv07TR6yClM_G5ccm_MU6kFAXL4",
    "user": {
        "id": "241",
        "nick": "nigiri",
        "avatar": "",
        "online": true
    }
}*/
    //private val args: HomeFragmentArgs by navArgs()
    private lateinit var mBinding: FragmentHomeBinding
    private lateinit var mAdapter: ContactsAdapter
    private val mViewModel = HomeFragmentViewModel(GetListProfilesUseCase(), GetListChatsUseCase())
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
        //mViewHolder.getUsersList(args.token)
        mViewModel.getUsersList("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjI0MSIsImlhdCI6MTcxMDQ5NzQ4NSwiZXhwIjoxNzEzMDg5NDg1fQ.YVwmg-Cn8X1oWDkNkv07TR6yClM_G5ccm_MU6kFAXL4")
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            mViewModel.users.collect {
                mAdapter.refreshData(it)
            }
        }
        lifecycleScope.launch {
            mViewModel.getUsersListError.collect {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        }
        lifecycleScope.launch {
            mViewModel.chats.collect {
                mAdapter.refreshData(it)
            }
        }
        lifecycleScope.launch {
            mViewModel.getChatsListError.collect {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
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