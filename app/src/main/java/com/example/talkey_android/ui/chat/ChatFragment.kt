package com.example.talkey_android.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.talkey_android.MainActivity
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
    private lateinit var mainActivity: MainActivity

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
        with(binding) {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
            ivSend.setOnClickListener {
                if (etMessage.text.toString().isNotEmpty()) {
                    lifecycleScope.launch {
                        chatFragmentViewModel.sendMessage(
                            args.token,
                            args.idChat,
                            args.idUser,
                            etMessage.text.toString()
                        )
                    }
                    etMessage.text?.clear()
                }
            }
            root.setOnClickListener {
                mainActivity.hideKeyBoard()
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            chatFragmentViewModel.message.collect { messages ->
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