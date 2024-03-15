package com.example.talkey_android.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.talkey_android.databinding.FragmentChatBinding
import com.example.talkey_android.ui.chat.adapter.ChatAdapter

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var linearLayout: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        initRecyclerView()
        return binding.root
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