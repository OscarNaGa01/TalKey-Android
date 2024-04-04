package com.example.talkey_android.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.talkey_android.MainActivity
import com.example.talkey_android.R
import com.example.talkey_android.data.domain.use_cases.chats.GetListChatsUseCase
import com.example.talkey_android.data.domain.use_cases.messages.GetListMessageUseCase
import com.example.talkey_android.data.domain.use_cases.messages.SendMessageUseCase
import com.example.talkey_android.databinding.FragmentChatBinding
import com.example.talkey_android.databinding.ItemProfileImageStatusChatBinding
import com.example.talkey_android.ui.chat.adapter.ChatAdapter
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var profileImageStatusBinding: ItemProfileImageStatusChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var linearLayout: LinearLayoutManager
    private val args: ChatFragmentArgs by navArgs()
    private val chatFragmentViewModel: ChatFragmentViewModel =
        ChatFragmentViewModel(SendMessageUseCase(), GetListMessageUseCase(), GetListChatsUseCase())
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        profileImageStatusBinding = ItemProfileImageStatusChatBinding.bind(binding.root)
        mainActivity = requireActivity() as MainActivity
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView()
        observeViewModel()
        initListeners()
        refreshConfig()
        getMessageList(false)
    }

    private fun initListeners() {
        with(binding) {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        findNavController().popBackStack()
                    }
                })
            ivSend.setOnClickListener {
                sendMessage()
            }
            rvChat.setOnClickListener {
                mainActivity.hideKeyBoard()
            }
            swipeToRefresh.setOnRefreshListener {
                getMessageList(true)
                swipeToRefresh.isRefreshing = false
            }
        }
    }

    private fun refreshConfig() {
        with(binding.swipeToRefresh) {
            setProgressBackgroundColorSchemeColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.background
                )
            )
            setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.vassBlue))
        }
    }

    private fun sendMessage() {
        with(binding) {
            if (etMessage.text.toString().isNotEmpty()) {
                chatFragmentViewModel.sendMessage(
                    args.idChat,
                    args.idUser,
                    etMessage.text.toString()
                )
                etMessage.text?.clear()
                rvChat.scrollToPosition(rvChat.top)
            }
        }
    }

    private fun getMessageList(isRefresh: Boolean) {
        chatFragmentViewModel.getContactData(args.idChat, args.idUser)
        chatFragmentViewModel.getMessages(args.idChat, isRefresh)
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            chatFragmentViewModel.contact.collect { userData ->

                binding.tvName.text = userData.targetNick

                Glide.with(requireContext())
                    .load("https://mock-movilidad.vass.es/${userData.targetAvatar}")
                    .apply(RequestOptions().centerCrop())
                    .error(R.drawable.perfil)
                    .into(profileImageStatusBinding.ivProfile)

                if (userData.targetOnline) {
                    profileImageStatusBinding.ivStatus.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.green_lime
                        )
                    )

                } else {
                    profileImageStatusBinding.ivStatus.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.statusOffline
                        )
                    )
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            chatFragmentViewModel.message.collect { messages ->
                chatAdapter.updateList(messages)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            chatFragmentViewModel.setMessageError.collect { error ->
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            chatFragmentViewModel.getListMessageError.collect { error ->
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            chatFragmentViewModel.getListChatsError.collect { error ->
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initRecyclerView() {
        chatAdapter = ChatAdapter(arrayListOf(), args.idUser)
        linearLayout = LinearLayoutManager(context)

        binding.rvChat.apply {
            adapter = chatAdapter
            layoutManager = linearLayout
        }
        setupPagination()
    }

    private fun setupPagination() {
        binding.rvChat.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager: LinearLayoutManager? = recyclerView.layoutManager as LinearLayoutManager?
                if (layoutManager?.findLastCompletelyVisibleItemPosition() == recyclerView.adapter?.itemCount?.minus(1)) {
                    getMessageList(false)
                }
            }
        })
    }
}