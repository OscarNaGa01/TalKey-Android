package com.example.talkey_android.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.talkey_android.MainActivity
import com.example.talkey_android.R
import com.example.talkey_android.data.domain.use_cases.chats.GetListChatsUseCase
import com.example.talkey_android.data.domain.use_cases.messages.GetListMessageUseCase
import com.example.talkey_android.data.domain.use_cases.messages.SendMessageUseCase
import com.example.talkey_android.data.utils.Utils
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
        getMessageList()
    }

    private fun initListeners() {
        with(binding) {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
            ivSend.setOnClickListener {
                sendMessage()
            }
            rvChat.setOnClickListener {
                mainActivity.hideKeyBoard()
            }
            swipeToRefresh.setOnRefreshListener {
                getMessageList()
                swipeToRefresh.isRefreshing = false
            }
        }
    }

    private fun sendMessage() {
        with(binding) {
            if (etMessage.text.toString().isNotEmpty()) {
                chatFragmentViewModel.sendMessage(
                    args.token,
                    args.idChat,
                    args.idUser,
                    etMessage.text.toString()
                )
                etMessage.text?.clear()
                rvChat.scrollToPosition(rvChat.top)
            }
        }
    }

    private fun getMessageList() {
        chatFragmentViewModel.getMessages(args.token, args.idChat, 20, 0)
        chatFragmentViewModel.getContactData(args.token, args.idChat)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            chatFragmentViewModel.contact.collect { userData ->
                binding.tvName.text = userData.targetNick

                Glide.with(requireContext())
                    .load(userData.targetAvatar)
                    .error(R.drawable.image)
                    .into(profileImageStatusBinding.ivProfile)
                println(userData.targetOnline)

                if (userData.targetOnline) {
                    Glide.with(requireContext())
                        .load(R.color.statusOnline)
                        .error(R.color.statusOnline)
                        .into(profileImageStatusBinding.ivStatus)

                } else {
                    Glide.with(requireContext())
                        .load(R.color.statusOffline)
                        .error(R.color.statusOffline)
                        .into(profileImageStatusBinding.ivStatus)
                }
            }
        }

        lifecycleScope.launch {
            chatFragmentViewModel.message.collect { messages ->
                Utils.showDateOnce(messages)
                chatAdapter.updateList(messages.rows)
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
    }
}