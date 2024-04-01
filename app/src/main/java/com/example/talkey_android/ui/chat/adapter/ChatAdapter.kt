package com.example.talkey_android.ui.chat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.talkey_android.R
import com.example.talkey_android.data.domain.model.messages.MessageModel
import com.example.talkey_android.databinding.ItemChatMeBinding
import com.example.talkey_android.databinding.ItemChatOtherBinding

class ChatAdapter(
    private var messageList: List<MessageModel>,
    private val idUser: String
) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private lateinit var context: Context

    companion object {
        const val SENT_MESSAGE = 0
        const val RECEIVED_MESSAGE = 1
    }

    inner class ViewHolder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(messageModel: MessageModel, itemViewType: Int) {
            when (itemViewType) {
                SENT_MESSAGE -> bindSentMessage(messageModel)
                RECEIVED_MESSAGE -> bindReceivedMessage(messageModel)
            }
        }

        private fun bindReceivedMessage(messageModel: MessageModel) {
            val currentBinding = binding as ItemChatOtherBinding
            val date = messageModel.day.split("-")
            with(currentBinding) {
                tvMessageOther.text = messageModel.message
                if (messageModel.day == "") {
                    tvDateOther.visibility = View.GONE
                } else {
                    tvDateOther.visibility = View.VISIBLE
                    tvDateOther.text =
                        context.getString(R.string.date_formater, date[0], date[1], date[2])
                }
                tvHourOther.text = messageModel.hour
            }
        }

        private fun bindSentMessage(messageModel: MessageModel) {
            val currentBinding = binding as ItemChatMeBinding
            val date = messageModel.day.split("-")
            with(currentBinding) {
                tvMessageMe.text = messageModel.message
                if (messageModel.day == "") {
                    tvDateMe.visibility = View.GONE
                } else {
                    tvDateMe.visibility = View.VISIBLE
                    tvDateMe.text =
                        context.getString(R.string.date_formater, date[0], date[1], date[2])
                }
                tvHourMe.text = messageModel.hour
            }
        }
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
        holder.bind(messageList[position], getItemViewType(position))
    }

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].source == idUser) {
            SENT_MESSAGE
        } else {
            RECEIVED_MESSAGE
        }
    }

    fun updateList(messages: List<MessageModel>) {
        messageList = messages
        notifyDataSetChanged()
    }
}