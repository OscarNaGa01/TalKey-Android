package com.example.talkey_android.ui.chat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.talkey_android.data.domain.model.messages.MessageModel
import com.example.talkey_android.databinding.ItemChatMeBinding
import com.example.talkey_android.databinding.ItemChatOtherBinding

class ChatAdapter(private val messageList: List<MessageModel>) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private lateinit var context: Context

    companion object {
        const val SENT_MESSAGE = 0
        const val RECEIVED_MESSAGE = 1
    }

    inner class ViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = when (viewType) {
            SENT_MESSAGE -> ItemChatMeBinding.inflate(LayoutInflater.from(context), parent, false)

            RECEIVED_MESSAGE -> ItemChatOtherBinding.inflate(LayoutInflater.from(context), parent, false)

            else -> ItemChatMeBinding.inflate(LayoutInflater.from(context), parent, false)
        }
        return (ViewHolder(binding))
    }

    override fun getItemCount(): Int = messageList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messageList[position]
    }
}