package com.example.talkey_android.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.talkey_android.MainActivity
import com.example.talkey_android.R
import com.example.talkey_android.data.domain.use_cases.chats.CreateChatUseCase
import com.example.talkey_android.data.domain.use_cases.chats.DeleteChatUseCase
import com.example.talkey_android.data.domain.use_cases.chats.GetListChatsUseCase
import com.example.talkey_android.data.domain.use_cases.messages.GetListMessageUseCase
import com.example.talkey_android.data.domain.use_cases.users.GetListProfilesUseCase
import com.example.talkey_android.data.domain.use_cases.users.SetOnlineUseCase
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

    private lateinit var mainActivity: MainActivity
    private val args: HomeFragmentArgs by navArgs()
    private var listType = ListType.CHATS
    private lateinit var mBinding: FragmentHomeBinding
    private lateinit var mAdapter: ContactsAdapter
    private lateinit var mViewModel: HomeFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = HomeFragmentViewModel(
            GetListProfilesUseCase(),
            GetListChatsUseCase(),
            GetListMessageUseCase(),
            CreateChatUseCase(),
            DeleteChatUseCase(),
            SetOnlineUseCase()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        mainActivity = requireActivity() as MainActivity
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
                    false
                )
            )
            true
        }
        mBinding.toolBar.menu.getItem(logoutIndex).setOnMenuItemClickListener {
            mViewModel.doLogout()
            findNavController().navigate(HomeFragmentDirections.actionHomeToLogin())
            true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchView()

        setupToggleBtnsAndGetList()

        setupAdapter()

        observeViewModel()

        setupSwipeToRefresh()

        with(mBinding) {
            constraintLayout.setOnClickListener {
                mainActivity.hideKeyBoard()
            }

        }
    }

    private fun setupSwipeToRefresh() {
        with(mBinding.swipeToRefresh) {
            setOnRefreshListener {
                when (listType) {
                    ListType.CHATS -> mViewModel.getChatsList(args.id)
                    ListType.CONTACTS -> mViewModel.getUsersList()
                }
                isRefreshing = false
            }
            setProgressBackgroundColorSchemeColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.background
                )
            )
            setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.vassBlue))
        }
    }

    private fun setupSearchView() {
        mBinding.searchView.setOnQueryTextListener(this)
        mBinding.searchView.setOnCloseListener {
            mViewModel.removeFilters(listType)
            true
        }
    }

    private fun setupToggleBtnsAndGetList() {
        val recyclerStartPosition = 0
        if (listType == ListType.CHATS) {
            mViewModel.getChatsList(args.id)
            setToggleBtnsChecks(mBinding.tbChats, mBinding.tbContacts)
        } else {
            mViewModel.getUsersList()
            setToggleBtnsChecks(mBinding.tbContacts, mBinding.tbChats)
        }

        mBinding.tbChats.setOnClickListener { _ ->
            setToggleBtnsChecks(mBinding.tbChats, mBinding.tbContacts)
            mViewModel.getChatsList(args.id)
            listType = ListType.CHATS
            mBinding.recyclerView.scrollToPosition(recyclerStartPosition)
        }
        mBinding.tbContacts.setOnClickListener { _ ->
            setToggleBtnsChecks(mBinding.tbContacts, mBinding.tbChats)
            mViewModel.getUsersList()
            listType = ListType.CONTACTS
            mBinding.recyclerView.scrollToPosition(recyclerStartPosition)
        }
    }

    private fun setToggleBtnsChecks(
        toggleBtnToCheckTrue: ToggleButton,
        toggleBtnToCheckFalse: ToggleButton
    ) {
        toggleBtnToCheckTrue.isChecked = true
        toggleBtnToCheckFalse.isChecked = false
    }


    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.deleteChatSuccess.collect {
                Log.i(">", "Muestra el toast de borrado exitoso")
                if (it) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.deleteChatSuccessfull), Toast.LENGTH_SHORT
                    ).show()
                }

                mViewModel.getChatsList(args.id)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.deleteChatError.collect {
                if (it.errorCode == 401.toString()) {
                    Log.i(">", "Muestra el toast de fallo en el borrado")
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.idNewChat.collect { idNewChat ->
                Log.i(">", "Realiza la navegación")
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeToChat(
                        args.id,
                        idNewChat
                    )
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.createNewChatError.collect {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.uiState.collect {
                when (it) {
                    is HomeFragmentUiState.Loading -> {
                        mBinding.progressBar.visibility = View.VISIBLE
                        mBinding.loadingBackground.visibility = View.VISIBLE
                    }

                    is HomeFragmentUiState.Success -> {
                        mBinding.progressBar.visibility = View.GONE
                        mBinding.loadingBackground.visibility = View.GONE
                        Log.i(">", "Refresca la lista del recycler")
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
        mAdapter = ContactsAdapter(requireContext(), this)
        val listManager = LinearLayoutManager(requireContext())

        with(mBinding) {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = listManager
            recyclerView.adapter = mAdapter
        }
    }

    override fun onClickContact(idContact: String) {
        mBinding.searchView.isIconified = true
        mViewModel.createChat(args.id, idContact)
    }

    override fun onClickChat(idChat: String, contactNick: String) {
        mBinding.searchView.isIconified = true
        Log.i(">", "Ha clicado en un chat")
        findNavController().navigate(
            HomeFragmentDirections.actionHomeToChat(
                args.id,
                idChat
            )
        )
    }

    override fun onLongClickChat(idChat: String) {
        showDialogToDeleteChat(idChat)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()) {
            mViewModel.filterListByName(query, listType)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?) = true

    private fun showDialogToDeleteChat(idChat: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.delete_confirmation_title))
        builder.setMessage(getString(R.string.delete_confirmation_question))

        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            mViewModel.deleteChat(idChat)
            Log.i(">", "AQUÍ ACEPTAS EL POP UP")
        }

        builder.setNegativeButton(getString(R.string.no)) { _, _ -> }

        val dialog = builder.create()
        dialog.show()
    }
}