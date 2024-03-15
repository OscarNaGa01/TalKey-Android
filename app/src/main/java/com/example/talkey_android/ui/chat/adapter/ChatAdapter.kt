package com.example.talkey_android.ui.chat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.talkey_android.R
import com.example.talkey_android.data.domain.model.common.MessageModel
import com.example.talkey_android.databinding.ItemChatMeBinding

class ChatAdapter(private val messageList: List<MessageModel>) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemChatMeBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_chat_me, parent, false)
        return (ViewHolder(view))
    }

    override fun getItemCount(): Int = messageList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messageList[position]
    }
}