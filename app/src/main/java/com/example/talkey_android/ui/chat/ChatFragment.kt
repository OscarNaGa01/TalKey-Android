package com.example.talkey_android.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.talkey_android.data.domain.use_cases.messages.GetListMessageUseCase
import com.example.talkey_android.data.domain.use_cases.messages.SendMessageUseCase
import com.example.talkey_android.databinding.FragmentChatBinding
import com.example.talkey_android.ui.chat.adapter.ChatAdapter
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var linearLayout: LinearLayoutManager
    private val args: ChatFragmentArgs by navArgs()
    private val chatFragmentViewModel: ChatFragmentViewModel =
        ChatFragmentViewModel(SendMessageUseCase(), GetListMessageUseCase())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        initRecyclerView()
        observeViewModel()
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        binding.ivSend.setOnClickListener {
            lifecycleScope.launch {
                chatFragmentViewModel.sendMessage(
                    args.token,
                    args.idChat,
                    args.idUser,
                    binding.etMessage.text.toString()
                )
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            chatFragmentViewModel.message.collect { messages ->

            }
        }
    }

    private fun initRecyclerView() {
        chatAdapter = ChatAdapter(listOf())
        linearLayout = LinearLayoutManager(context)

        binding.rvChat.apply {
            adapter = chatAdapter
            layoutManager = linearLayout
        }
    }
}